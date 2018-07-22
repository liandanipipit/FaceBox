package com.pipitliandani.android.facebox;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class InputData extends AppCompatActivity {
    Spinner unit, workUnit, officials, type;
    EditText name, nik, functionTitle, email, bDay, placeBirth, edulevel, phone, major, pengurusIKL, dapen;
    Button save, choose;
    ImageView photo;
    RadioGroup rg, head;
    RadioButton l, p, yes, no;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    private static int RESULT_LOAD_IMAGE = 1;
    Uri filePath;
    //    String url_image;
    DatabaseReference newRef;
    StorageReference sRefPhoto;
    ProgressDialog pd;
    int selectedItem, selItem;
    String nikKey, nameKey, functionTitleKey, dateMonthkey, emailKey,
     bDayKey, pBirth, eduLevelKey, phoneKey, IKLKey, dpnKey, majorKey, unitKey,
    workUnitKey, officialKey, url_image, typeKey, gender;

    String dataKey = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);
        calendar = Calendar.getInstance();
        bDay = findViewById(R.id.inputBirthday);
        unit = findViewById(R.id.unitSpinner);
        workUnit = findViewById(R.id.workUnitSpinner);
        officials = findViewById(R.id.officialSpinner);
        type = findViewById(R.id.typeSpinner);
        rg = findViewById(R.id.rg);
        l = findViewById(R.id.radioMale);
        p = findViewById(R.id.radioFemale);
        head = findViewById(R.id.head);
        yes = findViewById(R.id.radioYes);
        no = findViewById(R.id.radioNo);
        pd = new ProgressDialog(this);



        final String[] unitItem = new String[]{"", "Direksi", "Asisten Direksi", "Komite Audit",
                "Sekretaris Perusahaan", "SPI", "Divisi Keuangan dan Akuntansi", "Div. SDM dan Umum",
                "Div. Investasi dan Manaj. Resiko", "Divisi BangBistek", "Divisi Produksi", "Divisi Logistik",
                "Div. Manaj. Strategi dan Operasi", "UB. Elhan", "UB. Sistem Transportasi", "Unit Bisnis TIKN",
                "UB. Energi dan Produk Ritel", "KOPKARLEN", "Pelayanan Kesehatan", "PT. Eltran Indonesia", "PT. Len Railway Systems", "PT. Surya Energi Indotama",
                "PT. Len Telekomunikasi Indonesia"};

        final String[] workUnitItem = new String[]{"", "Komisaris Utama", "Komisaris", "Direktorat Utama", "Direktorat Operasi I",
                "Direktorat Operasi II", "Direktorat Keuangan_SDM", "Asisten Direksi", "Komite Audit", "Supervisor",
                "Komite Manajemen Resiko", "Bag. Pemasaran dan  Penjualan", "Bag. Produksi Elektronik dan Mekanik",
                "Bag. Pengawasan Keuangan", "Bag. Manajemen Proyek", "Bag. Pengembangan Bisnis", "Bag. Sistem Informasi",
                "Bag. Urusan Umum", "Bag. Komunikasi Korporasi", "Bag. Rek. Sistem dan  Manaj. Proyek",
                "Pengembangan Bisnis", "Bid. Rekayasa Sistem dan Komersial", "Engineering", "Bag. Manajemen Risiko",
                "Bag. Akuntansi", "Bag. Pengend. Persediaan dan Op. Gudang", "Bag. Komersial", "Bag. Kontrol dan Elektronika Daya",
                "Bag. Pengadaan Luar Negeri", "Bid. Operasi Matra Udara", "Bid. Operasi Matra Darat", "Project Op. & Procur.",
                "Bag. Perpajakan", "Bag. Perencanaan dan Evaluasi Korporasi", "Staff Ahli Dirtek", "Pelayanan Teknis",
                "Bag. RenDal Produksi", "Internal Control", "Bag. Inves. & Peng. Anak Usaha", "Pelayanan Kesehatan",
                "Departemen Engineering", "Dana Pensiun PT. Len Industri", "Bag. Pengadaan Dalam Negeri",
                "Bag. Rekayasa Produksi dan Jasa Produksi", "Bag. Produksi Modul Surya",
                "Bag. Rekayasa Sistem", "Bag. Sumber Daya Manusia", "Finance & Accounting", "Pusat",
                "Bag. Telekomunikasi dan Informatika", "Bag. Legal", "Dept. Pemasaran & Penjualan", "Marketing",
                "Bag. Administrasi Kontrak", "Sekretaris Perusahaan", "SPI", "Divisi Keuangan dan Akuntansi", "Div. SDM dan Umum",
                "Div. Investasi dan Manaj. Resiko", "Divisi BangBistek", "Divisi Produksi", "Divisi Logistik",
                "Div. Manaj. Strategi dan Operasi", "UB. Elhan", "Bid. Admin Ka UB Elhan", "UB. Sistem Transportasi", "Unit Bisnis TIKN",
                "Divisi Engineering", "Divisi Project", "Divisi Sales", "Divisi POP", "KOPKARLEN", "Pertokoan",
                "Bid. Admin Ka UB Elhan", "Sekretariat U.B. Sistem Transportasi",
                "UB. Energi dan Produk Ritel", " Direksi PT. Eltran Indonesia", "Rekayasa PT. Eltran Indonesia", "Manaj. Proyek PT. Eltran",
                "Direksi PT. Len Railway System", "Sekretariat Direksi PT. LRS", "Direksi PT. Surya Energi Indotama", "Direksi PT. LTI"};
        final String[] officialsItem = new String[]{"", "Komisaris", "Direksi Len", "Direksi Anak Perusahaan", "Eselon I", "Eselon II", "Asdir", "Komite", "Staff Ahli Dirtek PT. LRS"};
        final String[] typeItem = new String[]{"Permanent Employee", "Retired Employed", "THL", "KWT"};

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, unitItem);
        ArrayAdapter<String> workUnitAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, workUnitItem);
        ArrayAdapter<String> officialAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, officialsItem);
        final ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, typeItem);

        officials.setAdapter(officialAdapter);
        unit.setAdapter(unitAdapter);
        workUnit.setAdapter(workUnitAdapter);
        type.setAdapter(typeAdapter);

        name = findViewById(R.id.inputNama);
        nik = findViewById(R.id.inputNIK);
        functionTitle = findViewById(R.id.inputFunctiontitle);
        email = findViewById(R.id.inputEmail);
        placeBirth = findViewById(R.id.inputPlaceofbirth);
        edulevel = findViewById(R.id.inputEduLevel);
        phone = findViewById(R.id.inputPhone);
        pengurusIKL = findViewById(R.id.inputPengurusIKL);
        dapen = findViewById(R.id.inputDapen);
        major = findViewById(R.id.inputMajor);
        save = findViewById(R.id.btnSave);
        choose = findViewById(R.id.btnChoose);
        photo = findViewById(R.id.inputimage);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        bDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InputData.this, date, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)).show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Uploading...");
                pd.setIndeterminate(true);
                pd.show();

                if(dataKey == null) {
                    inputData();
                } else {
                    updateData();
                }

            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if (extra != null) {
            String key = extra.getString("key","");
            dataKey = key;
            Log.d("KEY_EDIT", key);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("employee");
            Query query = reference.child(key);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FaceBoxModel currentModel = dataSnapshot.getValue(FaceBoxModel.class);
                    nik.setText(currentModel.getNik());
                    name.setText(currentModel.getName());

                    String currentUnit = currentModel.getUnit();
                    for (int i = 0; i < unitItem.length; i++) {
                        if (unitItem[i].equals(currentUnit)) {
                            unit.setSelection(i);
                            break;
                        }
                    }

                    String currentWorkUnit = currentModel.getWorkUnit();
                    for (int i = 0; i < workUnitItem.length; i++) {
                        if (workUnitItem[i].equals(currentWorkUnit)) {
                            workUnit.setSelection(i);
                            break;
                        }
                    }
                    String currentOfficials = currentModel.getOfficials();
                    for (int i = 0; i < officialsItem.length; i++) {
                        if (officialsItem[i].equals(currentOfficials)) {
                            officials.setSelection(i);
                            break;
                        }
                    }

                    if (currentModel.isRetired()) {
                        type.setSelection(1);
                    } else if (currentModel.isThl()) {
                        type.setSelection(2);
                    } else if (currentModel.isKwl()) {
                        type.setSelection(3);
                    } else {
                        type.setSelection(0);
                    }

                    functionTitle.setText(currentModel.getFunctionTitle());
                    email.setText(currentModel.getEmail());
                    bDay.setText(currentModel.getBirthDate());
                    placeBirth.setText(currentModel.getPlaceOfBirth());
                    phone.setText(currentModel.getPhone());
                    edulevel.setText(currentModel.getEduLevel());
                    major.setText(currentModel.getMajor());
                    pengurusIKL.setText(currentModel.getIkl());
                    dapen.setText(currentModel.getPensionBudget());
                    rg.check(currentModel.getGender().equals("L") ? R.id.radioMale : R.id.radioFemale);
                    head.check(currentModel.isHead() ? R.id.radioYes : R.id.radioNo);
                    if (currentModel.image_url != null){
                        filePath = Uri.parse(currentModel.image_url);
                    }
                    Picasso.with(getApplicationContext()).load(currentModel.image_url).placeholder(R.color.grey).error(R.mipmap.ic_launcher).into(photo);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }

    }

    public void inputData() {
        sRefPhoto = FirebaseStorage.getInstance().getReference().child("newProfilePhoto/" + name.getText() + ".jpg");
        StorageReference photoImagesRef = FirebaseStorage.getInstance().getReference().child("newProfilePhoto/" + name.getText() + ".jpg");
        sRefPhoto.getName().equals(photoImagesRef.getName());
        sRefPhoto.getPath().equals(photoImagesRef.getPath());

        photo.setDrawingCacheEnabled(true);
        photo.buildDrawingCache();
        Bitmap bitmap = photo.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] dataByte = baos.toByteArray();

        UploadTask uploadTask = sRefPhoto.putBytes(dataByte);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath = taskSnapshot.getDownloadUrl();
                UploadTask();

            }
        });
    }

    public void updateData() {
        if(photo.getDrawingCache() != null) {
            sRefPhoto = FirebaseStorage.getInstance().getReference().child("newProfilePhoto/" + name.getText() + ".jpg");
            StorageReference photoImagesRef = FirebaseStorage.getInstance().getReference().child("newProfilePhoto/" + name.getText() + ".jpg");
            sRefPhoto.getName().equals(photoImagesRef.getName());
            sRefPhoto.getPath().equals(photoImagesRef.getPath());

            photo.setDrawingCacheEnabled(true);
            photo.buildDrawingCache();
            Bitmap bitmap = photo.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] dataByte = baos.toByteArray();

            UploadTask uploadTask = sRefPhoto.putBytes(dataByte);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath = taskSnapshot.getDownloadUrl();
                    UploadTask();

                }
            });
        } else {
            UploadTask();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void updateLabel() {
        String format = "dd/MM/yyyy";
        simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        bDay.setText(simpleDateFormat.format(calendar.getTime()));

    }



    public void UploadTask() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("employee");


        int selectedItem = rg.getCheckedRadioButtonId();
        int selItem = head.getCheckedRadioButtonId();
        nameKey = name.getText().toString();
        nikKey = nik.getText().toString();
        functionTitleKey = functionTitle.getText().toString();
        dateMonthkey = bDay.getText().toString().substring(0, 4);
        emailKey = email.getText().toString();
        bDayKey = bDay.getText().toString();
        pBirth = placeBirth.getText().toString();
        eduLevelKey = edulevel.getText().toString();
        phoneKey = phone.getText().toString();
        IKLKey = pengurusIKL.getText().toString();
        dpnKey = dapen.getText().toString();
        majorKey = major.getText().toString();
        unitKey = unit.getSelectedItem().toString();
        workUnitKey = workUnit.getSelectedItem().toString();
        officialKey = officials.getSelectedItem().toString();
        url_image = filePath.toString();
        typeKey = type.getSelectedItem().toString();


        gender = "";
        if (selectedItem == l.getId()) {
            gender = "L";
        } else {
            gender = "P";
        }

        FaceBoxModel data = new FaceBoxModel();
        data.setName(nameKey);
        data.setNik(nikKey);
        data.setFunctionTitle(functionTitleKey);
        data.setGender(gender);
        data.setEmail(emailKey);
        data.setBirthDate(bDayKey);
        data.setPlaceOfBirth(pBirth);
        data.setEduLevel(eduLevelKey);
        data.setPhone(phoneKey);
        data.setIkl(IKLKey);
        data.setDateMonthBirth(dateMonthkey);
        data.setPensionBudget(dpnKey);
        data.setMajor(majorKey);
        data.setUnit(unitKey);
        data.setImage_url(url_image);
        data.setWorkUnit(workUnitKey);
        data.setOfficials(officialKey);
        data.setLimit("-/-");
        if (unitKey.equals("KOPKARLEN")) {
            data.setHaveCoop(true);
            data.setCoop("KOPKARLEN");
        }
        Log.d("dpn", dpnKey);
        if (!dpnKey.equals("") && !dpnKey.equals("-")) {
            data.setHavePensionBudget(true);
        } else {
            data.setHavePensionBudget(false);
        }
        if (!IKLKey.equals("") && !IKLKey.equals("-")) {
            data.setHaveIKL(true);
        } else {
            data.setHaveIKL(false);
        }

        if (typeKey.equals("Retired")) {
            data.setRetired(true);
        } else if (typeKey.equals("KWT")){
            data.setKwl(true);
        } else if (typeKey.equals("THL")){
            data.setThl(true);
        }else {
            data.setThl(false);
            data.setKwl(false);
            data.setRetired(false);
        }
        if (selItem == yes.getId()){
            data.setHead(true);
        }else {
            data.setHead(false);
        }
        DatabaseReference newRef;
        if(dataKey == null) {
            newRef = ref.push();
        } else {
            newRef = ref.child(dataKey);
        }
        newRef.setValue(data);
        pd.dismiss();
        if (dataKey == null){
            Toast.makeText(InputData.this, "Saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(InputData.this, "Updated", Toast.LENGTH_LONG).show();
        }
        finish();
    }

}

