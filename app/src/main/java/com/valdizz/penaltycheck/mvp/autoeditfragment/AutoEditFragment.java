package com.valdizz.penaltycheck.mvp.autoeditfragment;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valdizz.penaltycheck.PenaltyCheckApplication;
import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.model.RealmService;
import com.valdizz.penaltycheck.model.entity.Auto;
import com.valdizz.penaltycheck.util.ImageUtils;
import com.valdizz.penaltycheck.util.CheckPermissionsUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.valdizz.penaltycheck.model.RealmService.AUTOID_PARAM;


public class AutoEditFragment extends Fragment implements AutoEditFragmentContract.View {

    @BindView(R.id.etSurname) TextInputEditText etSurname;
    @BindView(R.id.etName) TextInputEditText etName;
    @BindView(R.id.etPatronymic) TextInputEditText etPatronymic;
    @BindView(R.id.etSeries) TextInputEditText etSeries;
    @BindView(R.id.etNumber) TextInputEditText etNumber;
    @BindView(R.id.etDescription) TextInputEditText etDescription;
    @BindView(R.id.switchAutocheck) SwitchCompat switchAutocheck;
    @BindView(R.id.auto_image) ImageView autoImage;
    @BindView(R.id.auto_image_button) ImageView autoImageButton;
    @Inject RealmService realmService;
    private AutoEditFragmentContract.Presenter autoEditFragmentPresenter;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;
    private int userChoice;
    private String currentPhotoPath;

    public AutoEditFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_autoedit, container, false);
        PenaltyCheckApplication.getComponent().injectAutoEditFragment(this);
        ButterKnife.bind(this, view);

        if (autoEditFragmentPresenter == null) {
            autoEditFragmentPresenter = new AutoEditFragmentPresenter(this, realmService);
        }

        if (getArguments() != null) {
            Auto auto = realmService.getAuto(getArguments().getLong(AUTOID_PARAM));
            etSurname.setText(auto.getSurname());
            etName.setText(auto.getName());
            etPatronymic.setText(auto.getPatronymic());
            etSeries.setText(auto.getSeries());
            etNumber.setText(auto.getNumber());
            etDescription.setText(auto.getDescription());
            switchAutocheck.setChecked(auto.isAutomatically());
            if (auto.getImage().length > 0) {
                autoImage.setImageBitmap(ImageUtils.convertBytesToImage(auto.getImage()));
                autoImageButton.setImageResource(R.drawable.ic_clear);
                autoImageButton.setTag(true);
            }
            else {
                autoImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.empty_car));
                autoImageButton.setImageResource(R.drawable.ic_add_a_photo);
                autoImageButton.setTag(false);
            }
        }
        else {
            etSurname.setText(null);
            etName.setText(null);
            etPatronymic.setText(null);
            etSeries.setText(null);
            etNumber.setText(null);
            etDescription.setText(null);
            switchAutocheck.setChecked(false);
            autoImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.empty_car));
            autoImageButton.setImageResource(R.drawable.ic_add_a_photo);
            autoImageButton.setTag(false);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        autoEditFragmentPresenter.closeRealm();
        super.onDestroy();
    }

    //add or remove photo
    @OnClick(R.id.auto_image_button)
    public void onPhotoClick() {
        if (!((Boolean) autoImageButton.getTag())) {
            View dialogImageView = getActivity().getLayoutInflater().inflate(R.layout.dialog_image, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setCancelable(true)
                    .setTitle(getString(R.string.dialog_addfoto))
                    .setView(dialogImageView);
            final AlertDialog dialogImage = builder.create();

            LinearLayout llCamera = dialogImageView.findViewById(R.id.dialog_camera);
            llCamera.setOnClickListener(view -> {
                dialogImage.dismiss();
                userChoice = REQUEST_IMAGE_CAPTURE;
                if (CheckPermissionsUtils.checkPermissionReadExternalStorage(getActivity())) {
                    autoEditFragmentPresenter.onShowCameraClick();
                }
            });
            LinearLayout llGallery = dialogImageView.findViewById(R.id.dialog_gallery);
            llGallery.setOnClickListener(view -> {
                dialogImage.dismiss();
                userChoice = REQUEST_IMAGE_GALLERY;
                if (CheckPermissionsUtils.checkPermissionReadExternalStorage(getActivity())) {
                    autoEditFragmentPresenter.onShowGalleryClick();
                }
            });
            dialogImage.show();
        }
        else {
            autoEditFragmentPresenter.onDeleteImageClick();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CheckPermissionsUtils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoice==REQUEST_IMAGE_CAPTURE)
                        autoEditFragmentPresenter.onShowCameraClick();
                    else if (userChoice==REQUEST_IMAGE_GALLERY)
                        autoEditFragmentPresenter.onShowGalleryClick();
                }
                break;
        }
    }
    @Override
    public void showCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            Uri photoURI = null;
            try {
                File photoFile = ImageUtils.createImageFile(getActivity());
                currentPhotoPath = photoFile.getAbsolutePath();
                photoURI = FileProvider.getUriForFile(getActivity(), "com.valdizz.fileprovider", photoFile);
            } catch (IOException e) {
                Log.e(PenaltyCheckApplication.TAG, "Take picture error: "+e.getLocalizedMessage());
                Snackbar.make(switchAutocheck, getString(R.string.error_createimage), Snackbar.LENGTH_LONG).show();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                intent.setClipData(ClipData.newRawUri("", photoURI));
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void showGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_IMAGE_GALLERY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bitmap bitmapCamera = null;
                    try {
                        bitmapCamera = ImageUtils.rotateImageIfRequired(BitmapFactory.decodeFile(currentPhotoPath), currentPhotoPath);
                    } catch (IOException e) {
                        Snackbar.make(switchAutocheck, getString(R.string.error_convertimage), Snackbar.LENGTH_LONG).show();
                        break;
                        //e.printStackTrace();
                    }
                    Bitmap photo = ImageUtils.scaleImage(bitmapCamera, ImageUtils.dpToPx(getActivity(), ImageUtils.IMAGE_MAX_SIDE));;
                    autoEditFragmentPresenter.onSelectFromCameraResult(photo);
                    break;
                case REQUEST_IMAGE_GALLERY:
                    Bitmap bitmapGallery = null;
                    if (data != null) {
                        try {
                            bitmapGallery= ImageUtils.rotateImageIfRequired(getActivity(), MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData()), data.getData());
                        } catch (IOException e) {
                            Snackbar.make(switchAutocheck, getString(R.string.error_convertimage), Snackbar.LENGTH_LONG).show();
                            break;
                            //e.printStackTrace();
                        }
                    }
                    Bitmap image = ImageUtils.scaleImage(bitmapGallery, ImageUtils.dpToPx(getActivity(), ImageUtils.IMAGE_MAX_SIDE));
                    autoEditFragmentPresenter.onSelectFromGalleryResult(image);
                    break;
            }
        }
    }

    @Override
    public void showCarPhoto(Bitmap bitmap) {
        autoImage.setImageBitmap(bitmap);
        autoImageButton.setImageResource(R.drawable.ic_clear);
        autoImageButton.setTag(true);
    }

    @Override
    public void deleteCarPhoto() {
        autoImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.empty_car));
        autoImageButton.setImageResource(R.drawable.ic_add_a_photo);
        autoImageButton.setTag(false);
    }

    @Override
    public void showErrorIfAutoExists() {
        Snackbar.make(switchAutocheck, getString(R.string.dialog_autoexists), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorFieldIsEmpty() {
        Snackbar.make(switchAutocheck, getString(R.string.dialog_controlauto), Snackbar.LENGTH_LONG).show();
    }

    //save auto
    @Override
    public void onSaveAutoClick() {
        Auto auto = new Auto();
        if (getArguments() != null) {
            auto.setId(getArguments().getLong(AUTOID_PARAM));
        }
        auto.setName(etName.getText().toString().trim().toUpperCase());
        auto.setSurname(etSurname.getText().toString().trim().toUpperCase());
        auto.setPatronymic(etPatronymic.getText().toString().trim().toUpperCase());
        auto.setSeries(etSeries.getText().toString().trim().toUpperCase());
        auto.setNumber(etNumber.getText().toString().trim().toUpperCase());
        auto.setDescription(etDescription.getText().toString().trim());
        auto.setAutomatically(switchAutocheck.isChecked());
        if ((Boolean) autoImageButton.getTag()) {
            auto.setImage(ImageUtils.convertBitmapToBytes(((BitmapDrawable)autoImage.getDrawable()).getBitmap()));
        }
        else {
            byte[] bytes = {};
            auto.setImage(bytes);
        }
        autoEditFragmentPresenter.onSaveAutoClick(auto);
    }

    @Override
    public void onClose() {
        getActivity().finish();
    }

}
