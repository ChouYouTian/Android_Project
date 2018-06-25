package com.gcm.haoye.goodclassmate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main2Activity extends Activity {

    protected Button submitbtnobj;
    private TextView dataStart, startDate, startTime;
    private EditText name, money, dataEnd, warning;
    private int saveYear, saveMonth, saveDay, saveHour, saveMinute;
    private boolean dateClick,timeClick;
    private Context context;
    private CommentsDataSource database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        context = this;
        findViews();
    }
    private View.OnClickListener SubmitBtn = new View.OnClickListener(){
        public void onClick(View v){

            if(dateClick == false) {
                Toast.makeText(context, "請設定日期", Toast.LENGTH_SHORT).show();
                return;
            }
            if(timeClick == false) {
                Toast.makeText(context, "請設定時間", Toast.LENGTH_SHORT).show();
                return;
            }
            String startDateStr = startDate.getText().toString();
            String startTimeStr = startTime.getText().toString();
            String nameStr = name.getText().toString();
            String moneyStr = money.getText().toString();
            String dataEndStr =dataEnd.getText().toString();
            String warningStr = warning.getText().toString();
            if (startDateStr.isEmpty() ||
                    startTimeStr.isEmpty() ||
                    nameStr.isEmpty() ||
                    moneyStr.isEmpty() ||
                    dataEndStr.isEmpty() ||
                    warningStr.isEmpty()
                    ){
                Toast.makeText(context, "資料未齊全", Toast.LENGTH_SHORT).show();
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
            Date date = null;
            try {
                date = sdf.parse(saveYear+"/"+saveMonth+"/"+saveDay+"/"+saveHour+"/"+saveMinute);
            } catch (ParseException e) {
                Toast.makeText(context, "時間格式有誤", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            int dataEnd =Integer.parseInt(dataEndStr);
            int warning = Integer.parseInt(warningStr);
            if(dataEnd<warning) {
                Toast.makeText(context, "通知時間比還款時間多是白痴嗎？", Toast.LENGTH_SHORT).show();
                return;
            }
            Comment comment =new Comment();
            comment.setName(nameStr);
            comment.setMoney(Integer.parseInt(moneyStr));
            comment.setDataend(dataEndStr);
            comment.setDatestart(date.getTime()+"");

            comment.setWedt(warningStr);

            System.err.println(comment.toString());
            database.open();
            database.insertComment(comment);
            setResult(MainActivity.REQUESTCODE);

            finish();
        }
    };


    protected void findViews() {
        database = new CommentsDataSource(context);
        startDate =findViewById(R.id.startDate_TextView);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Calendar c = Calendar.getInstance();
                int mYear, mMonth, mDay;
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(
                        Main2Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        startDate.setText(year + " 年 "+ month + " 月 " + day + " 日 ");
                        saveYear = year;
                        saveMonth = month;
                        saveDay = day;
                        dateClick = true;
                    }
                }, mYear, mMonth, mDay).show();
            }
        });
        startTime = findViewById(R.id.startTime_TextView);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int  mHour, mMinute;
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                new TimePickerDialog(Main2Activity.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startTime.setText(hourOfDay + " 時 " + minute + " 分 ");
                        saveHour = hourOfDay;
                        saveMinute = minute;
                        timeClick = true;
                    }
                }, mHour, mMinute, true).show();
            }
        });

        name = findViewById(R.id.user_EditText);
        money = findViewById(R.id.money_EditText);
        dataEnd =findViewById(R.id.dataend_EditText);
        warning = findViewById(R.id.warning_EditText);

        submitbtnobj = findViewById(R.id.submitbtn);
        submitbtnobj.setOnClickListener(SubmitBtn);

    }
}
