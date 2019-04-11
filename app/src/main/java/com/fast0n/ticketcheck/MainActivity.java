package com.fast0n.ticketcheck;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

public class MainActivity extends AppCompatActivity {

    TextView show;
    FloatingActionButton fab;
    String a, b, c, d, e, f, g, h, i;

    CardView cardView;
    ImageView imageView;
    Button button;
    TextView tvNPrezzo, tvNNticket, tvNEMT, tvNCorsa, tvNTipo, tvNOra, tvNArrivo, tvNPartenza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Activity activity = this;

        button = findViewById(R.id.button);

        tvNPrezzo = findViewById(R.id.tvNPrice);
        tvNNticket = findViewById(R.id.tvNNticket);
        tvNEMT = findViewById(R.id.tvNEMT);
        tvNCorsa = findViewById(R.id.tvNCorsa);
        tvNTipo = findViewById(R.id.tvNTipo);
        tvNOra = findViewById(R.id.tvNOra);
        tvNArrivo = findViewById(R.id.tvNArrivo);
        tvNPartenza = findViewById(R.id.tvNPartenza);

        imageView = findViewById(R.id.imageView);
        cardView = findViewById(R.id.ticket);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            IntentIntegrator integrator = new IntentIntegrator(activity);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("");
            integrator.setOrientationLocked(false);
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        show = findViewById(R.id.textView);


        if (result != null) {
            if (result.getContents() == null) {
                //
            } else {
                String result_qr = result.getContents();


                Log.e("givemeaqr", result.getContents());
                try {

                    String[] array = result_qr.split("\\s+");

                    a = array[0];
                    b = array[1];
                    c = array[2];
                    d = array[3];
                    e = array[4];
                    f = array[5];
                    g = array[6];
                    h = array[7];
                    i = array[8];

                    String corsa = b.substring(0, 6);
                    String emt = c.substring(4, 12);


                    String day = b.substring(6, 14);
                    day = day.substring(0, 2) + "/" + day.substring(2, 4) + "/" + day.substring(4, 8);
                    String hour = i.substring(3, 7);
                    hour = hour.substring(0, 2) + ":" + hour.substring(2, 4);

                    String date = day + "\n" + hour;

                    String number_ticket = g.substring(0, 6);
                    String price = i.substring(0, 3);
                    price = price.substring(0, 1) + "." + price.substring(1, 3);


                    //tvNPartenza.setText("S.Luca Largo Ariel");
                    // tvNArrivo.setText("Bovalino M. Stazion");

                    tvNPartenza.setText("Null");
                    tvNArrivo.setText("Null");

                    tvNTipo.setText("Andata/Ritorno");
                    tvNCorsa.setText(corsa);
                    tvNEMT.setText(emt);
                    tvNNticket.setText(number_ticket);
                    tvNOra.setText(date);
                    tvNPrezzo.setText(price);

                    show.setVisibility(View.GONE);
                    cardView.setVisibility(View.VISIBLE);


                } catch (Exception ignored) {
                    show.setVisibility(View.GONE);
                    cardView.setVisibility(View.VISIBLE);
                }


                button.setOnClickListener(view -> {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND).setType("text/html").putExtra(Intent.EXTRA_TEXT, "<qr>" + result.getContents() + "</qr>");
                    startActivity(Intent.createChooser(sharingIntent, "Condividi con..."));
                });


                QRCodeWriter writer = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(result.getContents(), BarcodeFormat.QR_CODE, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    (imageView).setImageBitmap(bmp);

                } catch (WriterException e) {
                    e.printStackTrace();
                }


                imageView.setOnClickListener(view -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    final AlertDialog dialog = builder.create();
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.go_pro_dialog_layout, null);
                    dialog.setView(dialogLayout);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


                    dialog.setOnShowListener(d -> {
                        ImageView image = dialog.findViewById(R.id.goProDialogImage);


                        try {
                            BitMatrix bitMatrix = writer.encode(result.getContents(), BarcodeFormat.QR_CODE, 512, 512);
                            int width = bitMatrix.getWidth();
                            int height = bitMatrix.getHeight();
                            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                            for (int x = 0; x < width; x++) {
                                for (int y = 0; y < height; y++) {
                                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                                }
                            }
                            (image).setImageBitmap(bmp);

                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                        image.setOnClickListener(view1 -> dialog.dismiss());
                    });


                    dialog.show();
                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.instagram.com/fast0n/")));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
