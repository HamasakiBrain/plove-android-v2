package com.octarine.plove.menu;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.text.Html;
//import android.util.Log;
//import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.octarine.plove.api.models.BagItem;
import com.octarine.plove.api.models.MenuModel;
import com.octarine.plove.R;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.bag.BagActivity;
import com.octarine.plove.catalog.CategoryActivity;

import org.parceler.Parcels;
import java.io.ByteArrayOutputStream;

import static com.octarine.plove.catalog.CategoryActivity.EXTRA_FROM;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MenuItemActivity extends AppCompatActivity {

    public static final String EXTRA_MENUITEM = "EXTRA_ITEM";

    ImageButton plusBtn;
    ImageButton minusBtn;
    Button btnAddToBasket;
    TextView textViewCount;
    MenuModel menuItem;
    Context context;
    ImageView saleImageView;
    TextView mTitle;
    TextView mBagTitle;

    String launch_from  = CategoryActivity.FROM_DELIVERY;

    public void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            v.vibrate(100);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitle =  toolbar.findViewById(R.id.textViewToolbarTitle);

        menuItem =  Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_MENUITEM));
        mBagTitle =  toolbar.findViewById(R.id.textViewBagTitle);
        mBagTitle.setOnClickListener(view -> {
            Intent i = new Intent(MenuItemActivity.this, BagActivity.class);
            startActivityForResult(i, 1);
        });
        ImageView imageViewShop =  toolbar.findViewById(R.id.imageViewShop);
        imageViewShop.setOnClickListener(view -> {
            Intent i = new Intent(MenuItemActivity.this, BagActivity.class);
            startActivityForResult(i, 1);
        });

        setTitle("Меню");
        TextView tvDetail = findViewById(R.id.textViewDetail);
        tvDetail.setText(menuItem.description);
        String title = menuItem.displayName;

        TextView tvTitleBlack = findViewById(R.id.textViewSaleTitle1);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvTitleBlack.setText(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvTitleBlack.setText(Html.fromHtml(title));

        }

        btnAddToBasket =  findViewById(R.id.buttonAddToBag);
        btnAddToBasket.setText(menuItem.price + " RUB");

        textViewCount =  findViewById(R.id.textViewCount);
        plusBtn = findViewById(R.id.buttonPlus);
        minusBtn =  findViewById(R.id.buttonMinus);


        plusBtn.setOnClickListener(view -> {
            vibrate();

            String from = PreferenceManager.getDefaultSharedPreferences(MenuItemActivity.this).getString("EXTRA_FROM", "0");
            boolean basket_empty = ApplicationController.getInstance().getBasket(null, "").size() == 0;

            if (!basket_empty && !from.equalsIgnoreCase(launch_from) && !from.equalsIgnoreCase("0")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuItemActivity.this);
                builder.setTitle("Внимание!");
                builder.setMessage("У Вас не пустая корзина и Вы добавляете в корзину позицию из другого типа меню. Очистить корзину и добавить?");
                builder.setPositiveButton("Oчистить и добавить", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    ApplicationController.getInstance().clearBasket();
                    PreferenceManager.getDefaultSharedPreferences(MenuItemActivity.this)
                            .edit()
                            .putString(EXTRA_FROM, launch_from)
                            .apply();
                    ApplicationController.getInstance().addToBasket((MenuModel) menuItem, mBagTitle);
                    updateBasket();
                });
                builder.setNegativeButton("Отмена", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
                AlertDialog alert = builder.create();
                alert.show();
                alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
                alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.plove_red));
            } else {
                PreferenceManager.getDefaultSharedPreferences(MenuItemActivity.this)
                        .edit()
                        .putString(EXTRA_FROM, launch_from)
                        .apply();
                ApplicationController.getInstance().addToBasket((MenuModel) menuItem, mBagTitle);
                updateBasket();
            }


        });
        minusBtn.setOnClickListener(view -> {
            vibrate();
            ApplicationController.getInstance().removeFromBasket((MenuModel) menuItem, mBagTitle);
            updateBasket();
        });

        saleImageView =  findViewById(R.id.imageViewItem);
        Glide.with(this)
                .load(menuItem.detailImage.android)
                
                .fitCenter()
                .placeholder(R.drawable.open_food_placeholder)
                .into(saleImageView);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (isStoragePermissionGranted(MenuItemActivity.this, null)) {
                Uri bmpUri = getImageUri(context, ((BitmapDrawable) saleImageView.getDrawable()).getBitmap(), Html.fromHtml(menuItem.displayName).toString() + " - P.Love");
                if (bmpUri != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Привет! Советую попробывать '" + Html.fromHtml(menuItem.displayName).toString() + "' в ресторане P.Love");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setType("image/*");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                }
            }
        });

        updateBasket();
    }

    private void updateBasket() {
        ApplicationController.getInstance().getBasket(mBagTitle, " руб ");
        BagItem inBasket = ApplicationController.getInstance().inBasket(menuItem.crm_id);
        if ( inBasket != null) {
            textViewCount.setText(inBasket.count);
        } else {
            textViewCount.setText("0");
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage, String title) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, title, null);
        return Uri.parse(path);
    }


    private final static int PERMISSION_REQUEST_CODE = 13;

    public static boolean isStoragePermissionGranted(Activity activity, Fragment fragment) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (fragment == null) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                } else {
                    fragment.requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                }
                return false;
            }
        } else {
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Uri bmpUri = getImageUri(context, ((BitmapDrawable) saleImageView.getDrawable()).getBitmap(), Html.fromHtml(menuItem.displayName).toString() + " - P.Love");
                if (bmpUri != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Привет! Советую попробывать '" + Html.fromHtml(menuItem.displayName).toString() + "' в ресторане P.Love");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setType("image/*");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                }

            }
        }
    }

}
