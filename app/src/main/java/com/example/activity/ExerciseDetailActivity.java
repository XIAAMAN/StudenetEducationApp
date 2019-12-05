package com.example.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bean.SysExercise;
import com.example.constant.Constant;
import com.example.db.SysExerciseDb;
import com.example.db.SysUserDb;
import com.example.fragment.AFragment;
import com.example.fragment.BFragment;
import com.example.studenteducation.MainActivity;
import com.example.studenteducation.R;
import com.example.util.AlertDialogUtil;
import com.example.util.OkhttpSubmitUtil;
import com.example.util.OkhttpTestUtil;
import com.example.util.TextViewHtml;
import com.example.util.UnionData;
import com.example.util.UploadUtil;
import com.facebook.stetho.common.StringUtil;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.ResponseBody;



public class ExerciseDetailActivity extends AppCompatActivity implements View.OnClickListener{
    final String DOC = "application/msword";
    final String XLS = "application/vnd.ms-excel";
    final String PPT = "application/vnd.ms-powerpoint";
    final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    final String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    final String PDF = "application/pdf";
    final String MP4 = "video/mp4";
    final String M3U8 = "application/x-mpegURL";
    public static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");

    private TextView exerciseName, inputExampleLabel, inputExample, outputExampleLabel, outputExample, warningInfoLabel, warningInfo;
    private Button chooseBtn, multiChooseBtn, judgeBtn, blankBtn, programBtn, selfBtn, submitExercise, testExercise, backExercise, nextExercise, chooseFile;
    private int chooseNum=0, multiChooseNum=0, judgeNum=0, blankNum=0, programNum=0, selfNum=0;
    private List<SysExercise> chooseList, multiChooseList, judgeList, blankList, programList, selfList, tempExerciseList, tempBlankList, exerciseList;
    private SysExercise currentExercise;
    private int exerciseLength = 0, exerciseIndex = 0, exerciseType = 0;
    private RadioGroup chooseGroup, judgeGroup;
    private RadioButton chooseA, chooseB, chooseC, chooseD, judgeRight, judgeError;
    private CheckBox checkBoxA, checkBoxB, checkBoxC, checkBoxD;
    private LinearLayout multiChooseLayout, blankGroup, programGroup, selfGroup;
    private EditText blankOne, blankTwo, blankThree, blankFour, blankFive, blankSix, blankSeven, blankEight, blankNine, blankTen, editProgram, submitOUtCome;
    private EditText fileName;
    private TextView decidedExercise;
    private Button exerciseBack;
    private String chooseFilePath, collectionId;
    private File file;
    private String tempFileName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_detail);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.blue));
        Intent intent = getIntent();
        collectionId = intent.getStringExtra("collectionId");


        chooseBtn = (Button)findViewById(R.id.choose_type);
        multiChooseBtn = (Button)findViewById(R.id.multi_choose_type);
        judgeBtn = (Button)findViewById(R.id.judge_type);
        blankBtn = (Button)findViewById(R.id.blank_type);
        programBtn = (Button)findViewById(R.id.program_type);
        selfBtn = (Button) findViewById(R.id.self_type);
        submitExercise = (Button) findViewById(R.id.submit_exercise);
        testExercise = (Button)findViewById(R.id.test_exercise);
        backExercise = (Button)findViewById(R.id.back_exercise);
        nextExercise = (Button)findViewById(R.id.next_exercise);

        exerciseName = (TextView)findViewById(R.id.exercise_name);
        inputExampleLabel = (TextView) findViewById(R.id.input_example_label);
        inputExample = (TextView) findViewById(R.id.input_example);
        outputExampleLabel = (TextView) findViewById(R.id.output_example_label);
        outputExample = (TextView) findViewById(R.id.output_example);
        warningInfoLabel = (TextView) findViewById(R.id.warning_info_label);
        warningInfo = (TextView) findViewById(R.id.waring_info);

        chooseGroup = (RadioGroup) findViewById(R.id.radioGroup_choose);
        chooseA = (RadioButton) findViewById(R.id.A_choose);
        chooseB = (RadioButton) findViewById(R.id.B_choose);
        chooseC = (RadioButton) findViewById(R.id.C_choose);
        chooseD = (RadioButton) findViewById(R.id.D_choose);

        checkBoxA = (CheckBox) findViewById(R.id.checkboxA);
        checkBoxB = (CheckBox) findViewById(R.id.checkboxB);
        checkBoxC = (CheckBox) findViewById(R.id.checkboxC);
        checkBoxD = (CheckBox) findViewById(R.id.checkboxD);

        judgeGroup = (RadioGroup) findViewById(R.id.judge_group);
        judgeRight = (RadioButton) findViewById(R.id.judge_right);
        judgeError = (RadioButton) findViewById(R.id.judge_error);

        blankGroup = (LinearLayout) findViewById(R.id.blank_group);
        blankOne = (EditText) findViewById(R.id.blank_one);
        blankTwo = (EditText) findViewById(R.id.blank_two);
        blankThree = (EditText) findViewById(R.id.blank_three);
        blankFour = (EditText) findViewById(R.id.blank_four);
        blankFive = (EditText) findViewById(R.id.blank_five);
        blankSix = (EditText) findViewById(R.id.blank_six);
        blankSeven = (EditText) findViewById(R.id.blank_seven);
        blankEight = (EditText) findViewById(R.id.blank_eight);
        blankNine = (EditText) findViewById(R.id.blank_nine);
        blankTen = (EditText) findViewById(R.id.blank_ten);

        programGroup = (LinearLayout) findViewById(R.id.program_group);
        editProgram = (EditText) findViewById(R.id.edit_program);

        multiChooseLayout = (LinearLayout) findViewById(R.id.multi_choose_layout);

        selfGroup = (LinearLayout) findViewById(R.id.self_group);
        fileName = (EditText) findViewById(R.id.file_name);
        chooseFile = (Button) findViewById(R.id.choose_file);

        submitOUtCome = (EditText) findViewById(R.id.submit_outcome);

        exerciseBack = (Button) findViewById(R.id.exercise_back);

        decidedExercise = (TextView) findViewById(R.id.decided_exercise);

        exerciseList = new SysExerciseDb().getAllExercise();
        dealExerciseTypeNum(exerciseList);

        chooseBtn.setOnClickListener(this);
        multiChooseBtn.setOnClickListener(this);
        judgeBtn.setOnClickListener(this);
        blankBtn.setOnClickListener(this);
        programBtn.setOnClickListener(this);
        selfBtn.setOnClickListener(this);
        submitExercise.setOnClickListener(this);
        testExercise.setOnClickListener(this);
        backExercise.setOnClickListener(this);
        nextExercise.setOnClickListener(this);
        chooseFile.setOnClickListener(this);
        exerciseBack.setOnClickListener(this);
    }

    //计算各种题型题目数量
    public void dealExerciseTypeNum(List<SysExercise> exerciseList) {
        SysExercise exercise;
        chooseList = new ArrayList<>();
        multiChooseList = new ArrayList<>();
        judgeList = new ArrayList<>();
        blankList = new ArrayList<>();
        programList = new ArrayList<>();
        selfList = new ArrayList<>();
        tempBlankList = new ArrayList<>();
        tempExerciseList = new SysExerciseDb().getAllExercise();
        for(int i=0; i<exerciseList.size(); i++) {
            if(exerciseList.get(i).getExerciseType()==1) {
                programList.add(exerciseList.get(i));
                programNum++;
            } else if(exerciseList.get(i).getExerciseType()==2){
                chooseList.add(exerciseList.get(i));
                chooseNum++;
            } else if(exerciseList.get(i).getExerciseType()==3){
                judgeList.add(exerciseList.get(i));
                judgeNum++;
            } else if(exerciseList.get(i).getExerciseType()==4){
                blankList.add(exerciseList.get(i));
                exercise = new SysExercise();
                exercise.setExerciseId(exerciseList.get(i).getExerciseId());
                exercise.setExerciseCode(exerciseList.get(i).getExerciseCode());
                exercise.setExerciseType(exerciseList.get(i).getExerciseType());
                tempBlankList.add(exercise);
                blankNum++;
            } else if(exerciseList.get(i).getExerciseType()==5){
                multiChooseList.add(exerciseList.get(i));
                multiChooseNum++;
            } else {
                selfList.add(exerciseList.get(i));
                selfNum++;
            }
        }
        //将填空题答案设置为空字符
//        for(int i=0; i<blankList.size(); i++) {
//            blankList.get(i).setExerciseCode("");
//        }

        if(chooseNum == 0) {
            chooseBtn.setVisibility(View.GONE);
        }
        if(multiChooseNum == 0) {
            multiChooseBtn.setVisibility(View.GONE);
        }
        if(blankNum == 0) {
            blankBtn.setVisibility(View.GONE);
        }
        if(judgeNum == 0) {
            judgeBtn.setVisibility(View.GONE);
        }
        if(programNum == 0) {
            programBtn.setVisibility(View.GONE);
        }
        if(selfNum == 0) {
            selfBtn.setVisibility(View.GONE);
        }
        chooseBtn.setText("单选("+chooseNum+"题)");
        multiChooseBtn.setText("多选("+multiChooseNum+"题)");
        judgeBtn.setText("判断("+judgeNum+"题)");
        blankBtn.setText("填空("+blankNum+"题)");
        programBtn.setText("编程("+programNum+"题)");
        selfBtn.setText("主观("+selfNum+"题)");
        if(chooseNum > 0) {
            exerciseType = 2;
            currentExercise = chooseList.get(exerciseIndex);
            exerciseLength = chooseList.size();
        }else if(multiChooseNum > 0) {
            exerciseType = 5;
            currentExercise = multiChooseList.get(exerciseIndex);
            exerciseLength = multiChooseList.size();
        } else if(judgeNum > 0) {
            exerciseType = 3;
            currentExercise = judgeList.get(exerciseIndex);
            exerciseLength = judgeList.size();
        } else if(blankNum > 0) {
            exerciseType = 4;
            currentExercise = blankList.get(exerciseIndex);
            exerciseLength = blankList.size();
        } else if(programNum > 0) {
            exerciseType = 1;
            currentExercise = programList.get(exerciseIndex);
            exerciseLength = programList.size();
            showInputAndOutput();
        } else{
            exerciseType = 6;
            currentExercise = selfList.get(exerciseIndex);
            exerciseLength = selfList.size();
        }
        TextViewHtml.htmlToText(exerciseName,this,currentExercise.getExerciseName(),false);
        checkDecidedExerciseNumber();
        judgeInputAndOutputLabel();
        showDifferentOut();
        judgeBackAndNextBtnState();
        judgeOnlineBtn();

    }

    //监听各个按钮点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_type:
                chooseChange();
                break;
            case R.id.multi_choose_type:
                multiChange();
                break;
            case R.id.judge_type:
                judgeChange();
                break;
            case R.id.blank_type:
                blankChange();
                break;
            case R.id.program_type:
                programChange();
                break;
            case R.id.self_type:
                selfChange();
                break;
            case R.id.submit_exercise:
                if(exerciseType == 6) {
                    String fileHint = fileName.getHint().toString();
                    Log.d("蓝牙", "文件名称2 ："+ fileHint);
                    if(fileHint.length()==0) {
                        new AlertDialogUtil("消息提示", "请选择文件", this).alertDialogWithOk();
                    } else if(fileHint.length()>4 && (fileHint.substring(fileHint.length()-4).equals(".doc") || fileHint.substring(fileHint.length()-5).equals(".docx"))) {
                        uploadFile();
                    } else if(fileHint.equals("上传成功") || fileHint.equals("已提交")) {
                        new AlertDialogUtil("消息提示", "请重新选择文件", this).alertDialogWithOk();
                    } else {
                        new AlertDialogUtil("消息提示", "暂不支持该类型文件上传", this).alertDialogWithOk();
                    }
                } else if(exerciseType == 4){
                    updateExerciseListCode();

                    if(isHasBlank()) {
                        notDisplaySubmitOUtCome();
                        submitExercise();
                    } else {
                        Toast.makeText(this, "请填写完所有空再提交", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    updateExerciseListCode();
                    if(currentExercise.getExerciseCode().length() > 0) {
                        notDisplaySubmitOUtCome();
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                //execute the task
                                submitExercise();
                            }
                        }, 200);

                    } else {
                        Toast.makeText(this, "请作答后再提交答案", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.test_exercise:
                updateExerciseListCode();
                if(currentExercise.getExerciseCode().length() > 0) {
                    notDisplaySubmitOUtCome();
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            //execute the task
                            onlineExercise();
                        }
                    }, 200);
                } else {
                    Toast.makeText(this, "请作答后再提交答案", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back_exercise:
                backExercise();
                break;
            case R.id.next_exercise:
                nextExercise();
                break;
            case R.id.choose_file:
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    showFileChooser();
                }
                break;
            case R.id.exercise_back:
//                onKeyDown(KeyEvent.KEYCODE_BACK, null);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    public void  chooseChange() {
        if(exerciseType != 2) {
            updateExerciseListCode();
            exerciseType = 2;
            currentExercise = chooseList.get(0);
            exerciseLength = chooseList.size();
            exerciseTypeChange();
        }
    }

    public void  judgeChange() {
        if(exerciseType != 3) {
            updateExerciseListCode();
            exerciseType = 3;
            currentExercise = judgeList.get(0);
            exerciseLength = judgeList.size();
            exerciseTypeChange();
        }
    }

    public void  blankChange() {
        if(exerciseType != 4) {
            updateExerciseListCode();
            exerciseType = 4;
            currentExercise = blankList.get(0);
            exerciseLength = blankList.size();
            exerciseTypeChange();
        }
    }

    public void  programChange() {
        if(exerciseType != 1) {
            updateExerciseListCode();
            exerciseType = 1;
            currentExercise = programList.get(0);
            exerciseLength = programList.size();
            exerciseTypeChange();
            showInputAndOutput();
        }
    }

    public void  multiChange() {
        if(exerciseType != 5) {
            updateExerciseListCode();
            exerciseType = 5;
            currentExercise = multiChooseList.get(0);
            exerciseLength = multiChooseList.size();
            exerciseTypeChange();
        }
    }

    public void  selfChange() {
        if(exerciseType != 6) {
            updateExerciseListCode();
            exerciseType = 6;
            currentExercise = selfList.get(0);
            exerciseLength = selfList.size();
            exerciseTypeChange();
        }
    }

    public void exerciseTypeChange() {
        TextViewHtml.htmlToText(exerciseName,this,currentExercise.getExerciseName(),false);
//        exerciseName.setText(Html.fromHtml(currentExercise.getExerciseName()));

        exerciseIndex = 0;
        judgeOnlineBtn();
        judgeBackAndNextBtnState();
        judgeInputAndOutputLabel();
        showDifferentOut();
    }

    //判断上一题和下一题的状态
    public void judgeBackAndNextBtnState() {
        if(exerciseIndex == 0) {
            backExercise.setBackground(getDrawable(R.drawable.btn_diaable));
            backExercise.setEnabled(false);
//            backExercise.setClickable(false);
            backExercise.setTextColor(getResources().getColor(R.color.fontDisable));
        } else {
            backExercise.setBackground(getDrawable(R.drawable.btn_enabled));
//            backExercise.setClickable(true);
            backExercise.setEnabled(true);
            backExercise.setTextColor(getResources().getColor(R.color.black));
        }

        if(exerciseIndex == (exerciseLength-1)) {
            nextExercise.setBackground(getDrawable(R.drawable.btn_diaable));
            nextExercise.setTextColor(getResources().getColor(R.color.fontDisable));
//            nextExercise.setClickable(false);
            nextExercise.setEnabled(false);
        } else {
            nextExercise.setBackground(getDrawable(R.drawable.btn_enabled));
            nextExercise.setTextColor(getResources().getColor(R.color.black));
//            nextExercise.setClickable(true);
            nextExercise.setEnabled(true);
        }
    }

    //根据题目类型判断是否隐藏在线运行
    public void judgeOnlineBtn() {
        if(exerciseType == 1) {
            testExercise.setVisibility(View.VISIBLE);
        } else {
            testExercise.setVisibility(View.GONE);
        }
    }

    //点击上一题的操作
    public void backExercise() {
        updateExerciseListCode();
        exerciseIndex--;
        setCurrentExercise();
        judgeBackAndNextBtnState();
        showDifferentOut();
    }

    //点击下一题的操作
    public void nextExercise() {
        updateExerciseListCode();       //更新当前题目答案
        exerciseIndex++;
        setCurrentExercise();
        judgeBackAndNextBtnState();
        showDifferentOut();     //显示当前答案

    }

    //设置当前题目
    public void setCurrentExercise() {
        if(exerciseType == 2) {
            currentExercise = chooseList.get(exerciseIndex);
        } else if(exerciseType == 5) {
            currentExercise = multiChooseList.get(exerciseIndex);
        } else if(exerciseType == 3) {
            currentExercise = judgeList.get(exerciseIndex);
        } else if(exerciseType == 4) {
            currentExercise = blankList.get(exerciseIndex);
        } else if(exerciseType == 1) {
            currentExercise = programList.get(exerciseIndex);
            showInputAndOutput();
        } else{
            currentExercise = selfList.get(exerciseIndex);
        }
        TextViewHtml.htmlToText(exerciseName,this,currentExercise.getExerciseName(),false);
//        exerciseName.setText(Html.fromHtml(currentExercise.getExerciseName()));
    }

    //判断是否显示输入输出样例标签
    public void judgeInputAndOutputLabel() {
        if (exerciseType == 1) {
            inputExampleLabel.setVisibility(View.VISIBLE);
            inputExample.setVisibility(View.VISIBLE);
            outputExampleLabel.setVisibility(View.VISIBLE);
            outputExample.setVisibility(View.VISIBLE);
            warningInfoLabel.setVisibility(View.VISIBLE);
            warningInfo.setVisibility(View.VISIBLE);
        } else {
            inputExampleLabel.setVisibility(View.GONE);
            inputExample.setVisibility(View.GONE);
            outputExampleLabel.setVisibility(View.GONE);
            outputExample.setVisibility(View.GONE);
            warningInfoLabel.setVisibility(View.GONE);
            warningInfo.setVisibility(View.GONE);
        }
    }

    //设置当前题目输入输出样例
    public void showInputAndOutput() {
//        TextViewHtml.htmlToText(inputExample,this,currentExercise.getExerciseInputExample(),false);
//        TextViewHtml.htmlToText(outputExample,this,currentExercise.getExerciseOutputExample(),false);
        inputExample.setText(Html.fromHtml(getHTMLStr(currentExercise.getExerciseInputExample())));
        outputExample.setText(Html.fromHtml(getHTMLStr(currentExercise.getExerciseOutputExample())));
        warningInfo.setText(currentExercise.getExerciseWarning());
    }

    //根据题目类型显示不同的答题内容
    public void showDifferentOut(){
        notDisplaySubmitOUtCome();
        if(exerciseType == 2) {
            displayChooseOUt();
            notDisplayMultiChooseOUt();
            notDisplayJudgeOUt();
            notDisplayBlankOUt();
            notDisplayProgramOUt();
            notDisplaySelfOUt();
        } else if(exerciseType == 5) {
            notDisplayChooseOUt();
            displayMultiChooseOUt();
            notDisplayJudgeOUt();
            notDisplayBlankOUt();
            notDisplayProgramOUt();
            notDisplaySelfOUt();
        } else if(exerciseType == 3) {
            notDisplayChooseOUt();
            notDisplayMultiChooseOUt();
            displayJudgeOUt();
            notDisplayBlankOUt();
            notDisplayProgramOUt();
            notDisplaySelfOUt();
        } else if(exerciseType == 4) {
            notDisplayChooseOUt();
            notDisplayMultiChooseOUt();
            notDisplayJudgeOUt();
            displayBlankOut();
            notDisplayProgramOUt();
            notDisplaySelfOUt();
        } else if(exerciseType == 1) {
            notDisplayChooseOUt();
            notDisplayMultiChooseOUt();
            notDisplayJudgeOUt();
            notDisplayBlankOUt();
            displayProgramOUt();
            notDisplaySelfOUt();
        } else{
            notDisplayChooseOUt();
            notDisplayMultiChooseOUt();
            notDisplayJudgeOUt();
            notDisplayBlankOUt();
            notDisplayProgramOUt();
            displaySelfOUt();
        }
    }

    //不显示选择题答题
    public void notDisplayChooseOUt() {
        chooseGroup.setVisibility(View.GONE);
    }

    //显示选择题答题
    public void displayChooseOUt() {
        chooseGroup.setVisibility(View.VISIBLE);
        //将当前保存答案赋值

        chooseGroup.clearCheck();
        if("A".equals(currentExercise.getExerciseCode())) {
            chooseA.setChecked(true);
        } else if("B".equals(currentExercise.getExerciseCode())) {
            chooseB.setChecked(true);
        } else if("C".equals(currentExercise.getExerciseCode())) {
            chooseC.setChecked(true);
        } else if("D".equals(currentExercise.getExerciseCode())) {
            chooseD.setChecked(true);
        } else {
            chooseA.setChecked(false);
            chooseB.setChecked(false);
            chooseC.setChecked(false);
            chooseD.setChecked(false);
        }

    }

    //不显示多选题答题
    public void notDisplayMultiChooseOUt() {
        multiChooseLayout.setVisibility(View.GONE);

    }

    //显示多选题答题
    public void displayMultiChooseOUt() {
        multiChooseLayout.setVisibility(View.VISIBLE);

        String []strList = currentExercise.getExerciseCode().split("");
        checkBoxA.setChecked(false);
        checkBoxB.setChecked(false);
        checkBoxC.setChecked(false);
        checkBoxD.setChecked(false);
        for(int i=0; i<strList.length; i++) {
            if("A".equals(strList[i])) {
                checkBoxA.setChecked(true);
            } else if("B".equals(strList[i])) {
                checkBoxB.setChecked(true);
            } else if("C".equals(strList[i])) {
                checkBoxC.setChecked(true);
            } else if("D".equals(strList[i])) {
                checkBoxD.setChecked(true);
            }
        }
    }

    //不显示判断题答题
    public void notDisplayJudgeOUt() {
        judgeGroup.setVisibility(View.GONE);
    }

    //显示判断题答题
    public void displayJudgeOUt() {
        judgeGroup.setVisibility(View.VISIBLE);
        //将当前保存答案赋值
        judgeGroup.clearCheck();
        if("对".equals(currentExercise.getExerciseCode())) {
            judgeRight.setChecked(true);
        } else if("错".equals(currentExercise.getExerciseCode())) {
            judgeError.setChecked(true);
        } else {
            judgeRight.setChecked(false);
            judgeError.setChecked(false);
        }
    }

    //不显示填空题答题
    public void notDisplayBlankOUt() {
        blankGroup.setVisibility(View.GONE);
    }

    //显示填空题答题
    public void displayBlankOut() {
        blankGroup.setVisibility(View.VISIBLE);

        //找到当前题目有多少空
//        String blanStr = "";
//        String str[], tempStr[] = new String[10];
//        int index = 0;
//        for(int i=0; i<tempBlankList.size(); i++) {
//            if(tempBlankList.get(i).getExerciseId().equals(currentExercise.getExerciseId())) {
//                blanStr = tempBlankList.get(i).getExerciseCode();
//                index = i;
//                break;
//            }
//        }
//        str = blankList.get(index).getExerciseCode().split(";xiaaman;");
//        for(int i=0; i<tempStr.length; i++) {
//            if(i<str.length) {
//                tempStr[i] = str[i];
//            } else {
//                tempStr[i] = "";
//            }
//        }

        int number = currentExercise.getExerciseClassifyCause();
        String tempStr[] = new String[number];
        String str;
        if(currentExercise.getExerciseCode().length() > 0) {
            str = currentExercise.getExerciseCode().replace(";xiaaman;",";xiaaman; ");
            tempStr = str.split(";xiaaman;");
            for(int i=0; i<tempStr.length; i++) {
                if(" ".equals(tempStr[i])) {
                    tempStr[i] = "";
                }
            }
        } else {
            for(int i=0; i<number;i++) {
                tempStr[i] = "";
            }
        }
        if(number == 1) {
            blankOne.setVisibility(View.VISIBLE);
            blankTwo.setVisibility(View.GONE);
            blankThree.setVisibility(View.GONE);
            blankFour.setVisibility(View.GONE);
            blankFive.setVisibility(View.GONE);
            blankSix.setVisibility(View.GONE);
            blankSeven.setVisibility(View.GONE);
            blankEight.setVisibility(View.GONE);
            blankNine.setVisibility(View.GONE);
            blankTen.setVisibility(View.GONE);
            blankOne.setText(tempStr[0]);
        }
        if(number == 2) {
            blankOne.setVisibility(View.VISIBLE);
            blankTwo.setVisibility(View.VISIBLE);
            blankThree.setVisibility(View.GONE);
            blankFour.setVisibility(View.GONE);
            blankFive.setVisibility(View.GONE);
            blankSix.setVisibility(View.GONE);
            blankSeven.setVisibility(View.GONE);
            blankEight.setVisibility(View.GONE);
            blankNine.setVisibility(View.GONE);
            blankTen.setVisibility(View.GONE);
            blankOne.setText(tempStr[0]);
            blankTwo.setText(tempStr[1]);
        }
        if(number == 3) {
            blankOne.setVisibility(View.VISIBLE);
            blankTwo.setVisibility(View.VISIBLE);
            blankThree.setVisibility(View.VISIBLE);
            blankFour.setVisibility(View.GONE);
            blankFive.setVisibility(View.GONE);
            blankSix.setVisibility(View.GONE);
            blankSeven.setVisibility(View.GONE);
            blankEight.setVisibility(View.GONE);
            blankNine.setVisibility(View.GONE);
            blankTen.setVisibility(View.GONE);
            blankOne.setText(tempStr[0]);
            blankTwo.setText(tempStr[1]);
            blankThree.setText(tempStr[2]);
        }
        if(number == 4) {
            blankOne.setVisibility(View.VISIBLE);
            blankTwo.setVisibility(View.VISIBLE);
            blankThree.setVisibility(View.VISIBLE);
            blankFour.setVisibility(View.VISIBLE);
            blankFive.setVisibility(View.GONE);
            blankSix.setVisibility(View.GONE);
            blankSeven.setVisibility(View.GONE);
            blankEight.setVisibility(View.GONE);
            blankNine.setVisibility(View.GONE);
            blankTen.setVisibility(View.GONE);
            blankOne.setText(tempStr[0]);
            blankTwo.setText(tempStr[1]);
            blankThree.setText(tempStr[2]);
            blankFour.setText(tempStr[3]);
        }
        if(number == 5) {
            blankOne.setVisibility(View.VISIBLE);
            blankTwo.setVisibility(View.VISIBLE);
            blankThree.setVisibility(View.VISIBLE);
            blankFour.setVisibility(View.VISIBLE);
            blankFive.setVisibility(View.VISIBLE);
            blankSix.setVisibility(View.GONE);
            blankSeven.setVisibility(View.GONE);
            blankEight.setVisibility(View.GONE);
            blankNine.setVisibility(View.GONE);
            blankTen.setVisibility(View.GONE);
            blankOne.setText(tempStr[0]);
            blankTwo.setText(tempStr[1]);
            blankThree.setText(tempStr[2]);
            blankFour.setText(tempStr[3]);
            blankFive.setText(tempStr[4]);
        }
        if(number == 6) {
            blankOne.setVisibility(View.VISIBLE);
            blankTwo.setVisibility(View.VISIBLE);
            blankThree.setVisibility(View.VISIBLE);
            blankFour.setVisibility(View.VISIBLE);
            blankFive.setVisibility(View.VISIBLE);
            blankSix.setVisibility(View.VISIBLE);
            blankSeven.setVisibility(View.GONE);
            blankEight.setVisibility(View.GONE);
            blankNine.setVisibility(View.GONE);
            blankTen.setVisibility(View.GONE);
            blankOne.setText(tempStr[0]);
            blankTwo.setText(tempStr[1]);
            blankThree.setText(tempStr[2]);
            blankFour.setText(tempStr[3]);
            blankFive.setText(tempStr[4]);
            blankSix.setText(tempStr[5]);
        }
        if(number == 7) {
            blankOne.setVisibility(View.VISIBLE);
            blankTwo.setVisibility(View.VISIBLE);
            blankThree.setVisibility(View.VISIBLE);
            blankFour.setVisibility(View.VISIBLE);
            blankFive.setVisibility(View.VISIBLE);
            blankSix.setVisibility(View.VISIBLE);
            blankSeven.setVisibility(View.VISIBLE);
            blankEight.setVisibility(View.GONE);
            blankNine.setVisibility(View.GONE);
            blankTen.setVisibility(View.GONE);
            blankOne.setText(tempStr[0]);
            blankTwo.setText(tempStr[1]);
            blankThree.setText(tempStr[2]);
            blankFour.setText(tempStr[3]);
            blankFive.setText(tempStr[4]);
            blankSix.setText(tempStr[5]);
            blankSeven.setText(tempStr[6]);
        }
        if(number == 8) {
            blankOne.setVisibility(View.VISIBLE);
            blankTwo.setVisibility(View.VISIBLE);
            blankThree.setVisibility(View.VISIBLE);
            blankFour.setVisibility(View.VISIBLE);
            blankFive.setVisibility(View.VISIBLE);
            blankSix.setVisibility(View.VISIBLE);
            blankSeven.setVisibility(View.VISIBLE);
            blankEight.setVisibility(View.VISIBLE);
            blankNine.setVisibility(View.GONE);
            blankTen.setVisibility(View.GONE);
            blankOne.setText(tempStr[0]);
            blankTwo.setText(tempStr[1]);
            blankThree.setText(tempStr[2]);
            blankFour.setText(tempStr[3]);
            blankFive.setText(tempStr[4]);
            blankSix.setText(tempStr[5]);
            blankSeven.setText(tempStr[6]);
            blankEight.setText(tempStr[7]);
        }
        if(number == 9) {
            blankOne.setVisibility(View.VISIBLE);
            blankTwo.setVisibility(View.VISIBLE);
            blankThree.setVisibility(View.VISIBLE);
            blankFour.setVisibility(View.VISIBLE);
            blankFive.setVisibility(View.VISIBLE);
            blankSix.setVisibility(View.VISIBLE);
            blankSeven.setVisibility(View.VISIBLE);
            blankEight.setVisibility(View.VISIBLE);
            blankNine.setVisibility(View.VISIBLE);
            blankTen.setVisibility(View.GONE);
            blankOne.setText(tempStr[0]);
            blankTwo.setText(tempStr[1]);
            blankThree.setText(tempStr[2]);
            blankFour.setText(tempStr[3]);
            blankFive.setText(tempStr[4]);
            blankSix.setText(tempStr[5]);
            blankSeven.setText(tempStr[6]);
            blankEight.setText(tempStr[7]);
            blankNine.setText(tempStr[8]);
        }
        if(number == 10) {
            blankOne.setVisibility(View.VISIBLE);
            blankTwo.setVisibility(View.VISIBLE);
            blankThree.setVisibility(View.VISIBLE);
            blankFour.setVisibility(View.VISIBLE);
            blankFive.setVisibility(View.VISIBLE);
            blankSix.setVisibility(View.VISIBLE);
            blankSeven.setVisibility(View.VISIBLE);
            blankEight.setVisibility(View.VISIBLE);
            blankNine.setVisibility(View.VISIBLE);
            blankTen.setVisibility(View.VISIBLE);
            blankOne.setText(tempStr[0]);
            blankTwo.setText(tempStr[1]);
            blankThree.setText(tempStr[2]);
            blankFour.setText(tempStr[3]);
            blankFive.setText(tempStr[4]);
            blankSix.setText(tempStr[5]);
            blankSeven.setText(tempStr[6]);
            blankEight.setText(tempStr[7]);
            blankNine.setText(tempStr[8]);
            blankTen.setText(tempStr[9]);
        }
    }

    //不显示编程题答题
    public void notDisplayProgramOUt() {
        programGroup.setVisibility(View.GONE);
    }

    //显示编程题答题
    public void displayProgramOUt() {
        programGroup.setVisibility(View.VISIBLE);
        editProgram.setText(currentExercise.getExerciseCode());
    }

    //不显示主观题答题
    public void notDisplaySelfOUt() {
        selfGroup.setVisibility(View.GONE);
    }

    //不显示答题反馈
    public void notDisplaySubmitOUtCome() {
        submitOUtCome.setVisibility(View.GONE);
    }

    //显示答题反馈
    public void displaySubmitOUtCome() {
        submitOUtCome.setVisibility(View.VISIBLE);
    }

    //显示主观题答题
    public void displaySelfOUt() {
        selfGroup.setVisibility(View.VISIBLE);
        if(currentExercise.getExerciseCode().length() > 0) {
            fileName.setHint("已提交");
        } else {
            fileName.setHint("请上传word文件");
        }

    }

    public void updateExerciseListCode() {

        if(exerciseType == 2) {
            if(chooseA.isChecked()) {
                currentExercise.setExerciseCode("A");
            } else if(chooseB.isChecked()) {
                currentExercise.setExerciseCode("B");
            } else if(chooseC.isChecked()) {
                currentExercise.setExerciseCode("C");
            } else if(chooseD.isChecked()) {
                currentExercise.setExerciseCode("D");
            }
//            for(int i=0; i<chooseList.size(); i++) {
//                if(chooseList.get(i).getExerciseId().equals(currentExercise.getExerciseId())) {
//                    chooseList.get(i).setExerciseCode(currentExercise.getExerciseCode());
//                    break;
//                }
//            }
        } else if(exerciseType == 5) {
            currentExercise.setExerciseCode("");
            if(checkBoxA.isChecked()) {
                currentExercise.setExerciseCode(currentExercise.getExerciseCode()+"A");
            }
            if(checkBoxB.isChecked()) {
                currentExercise.setExerciseCode(currentExercise.getExerciseCode()+"B");
            }
            if(checkBoxC.isChecked()) {
                currentExercise.setExerciseCode(currentExercise.getExerciseCode()+"C");
            }
            if(checkBoxD.isChecked()) {
                currentExercise.setExerciseCode(currentExercise.getExerciseCode()+"D");
            }
//            for(int i=0; i<multiChooseList.size(); i++) {
//                if(multiChooseList.get(i).getExerciseId().equals(currentExercise.getExerciseId())) {
//                    multiChooseList.get(i).setExerciseCode(currentExercise.getExerciseCode());
//                    break;
//                }
//            }
        } else if(exerciseType == 3) {
            if(judgeRight.isChecked()) {
                currentExercise.setExerciseCode("对");
            } else if(judgeError.isChecked()) {
                currentExercise.setExerciseCode("错");
            }
//            for(int i=0; i<judgeList.size(); i++) {
//                if(judgeList.get(i).getExerciseId().equals(currentExercise.getExerciseId())) {
//                    judgeList.get(i).setExerciseCode(currentExercise.getExerciseCode());
//                    break;
//                }
//            }
        } else if(exerciseType == 4) {
            tempSaveBlankAnswer();
        } else if(exerciseType == 1) {
            currentExercise.setExerciseCode(editProgram.getText().toString());
        } else{
            if(fileName.getText().toString().length() > 0) {
                currentExercise.setExerciseCode((fileName.getText().toString()));
            }

        }
    }

    //将填空题答案缓存到code中
    public void tempSaveBlankAnswer() {
        //找到当前题目有多少空
        int index=0;
        for(int i=0; i<blankList.size(); i++) {
            if(blankList.get(i).getExerciseId().equals(currentExercise.getExerciseId())) {
                index = i;
                break;
            }
        }
        int number = currentExercise.getExerciseClassifyCause();
        if(number == 1) {
            blankList.get(index).setExerciseCode(blankOne.getText().toString());
        }
        if(number == 2) {
            blankList.get(index).setExerciseCode(blankOne.getText().toString() + ";xiaaman;" + blankTwo.getText().toString());
        }
        if(number == 3) {
            blankList.get(index).setExerciseCode(blankOne.getText().toString() + ";xiaaman;" + blankTwo.getText().toString()
                    + ";xiaaman;" + blankThree.getText().toString());
        }
        if(number == 4) {
            blankList.get(index).setExerciseCode(blankOne.getText().toString() + ";xiaaman;" + blankTwo.getText().toString()
                    + ";xiaaman;" + blankThree.getText().toString() + ";xiaaman;" + blankFour.getText().toString());
        }
        if(number == 5) {
            blankList.get(index).setExerciseCode(blankOne.getText().toString() + ";xiaaman;" + blankTwo.getText().toString()
                    + ";xiaaman;" + blankThree.getText().toString() + ";xiaaman;" + blankFour.getText().toString()
                    + ";xiaaman;" + blankFive.getText().toString());
        }
        if(number == 6) {
            blankList.get(index).setExerciseCode(blankOne.getText().toString() + ";xiaaman;" + blankTwo.getText().toString()
                    + ";xiaaman;" + blankThree.getText().toString() + ";xiaaman;" + blankFour.getText().toString()
                    + ";xiaaman;" + blankFive.getText().toString() + ";xiaaman;" + blankSix.getText().toString());
        }
        if(number == 7) {
            blankList.get(index).setExerciseCode(blankOne.getText().toString() + ";xiaaman;" + blankTwo.getText().toString()
                    + ";xiaaman;" + blankThree.getText().toString() + ";xiaaman;" + blankFour.getText().toString()
                    + ";xiaaman;" + blankFive.getText().toString() + ";xiaaman;" + blankSix.getText().toString()
                    + ";xiaaman;" + blankSeven.getText().toString());
        }
        if(number == 8) {
            blankList.get(index).setExerciseCode(blankOne.getText().toString() + ";xiaaman;" + blankTwo.getText().toString()
                    + ";xiaaman;" + blankThree.getText().toString() + ";xiaaman;" + blankFour.getText().toString()
                    + ";xiaaman;" + blankFive.getText().toString() + ";xiaaman;" + blankSix.getText().toString()
                    + ";xiaaman;" + blankSeven.getText().toString() + ";xiaaman;" + blankEight.getText().toString());
        }
        if(number == 9) {
            blankList.get(index).setExerciseCode(blankOne.getText().toString() + ";xiaaman;" + blankTwo.getText().toString()
                    + ";xiaaman;" + blankThree.getText().toString() + ";xiaaman;" + blankFour.getText().toString()
                    + ";xiaaman;" + blankFive.getText().toString() + ";xiaaman;" + blankSix.getText().toString()
                    + ";xiaaman;" + blankSeven.getText().toString() + ";xiaaman;" + blankEight.getText().toString()
                    + ";xiaaman;" + blankNine.getText().toString());
        }
        if(number == 10) {
            blankList.get(index).setExerciseCode(blankOne.getText().toString() + ";xiaaman;" + blankTwo.getText().toString()
                    + ";xiaaman;" + blankThree.getText().toString() + ";xiaaman;" + blankFour.getText().toString()
                    + ";xiaaman;" + blankFive.getText().toString() + ";xiaaman;" + blankSix.getText().toString()
                    + ";xiaaman;" + blankSeven.getText().toString() + ";xiaaman;" + blankEight.getText().toString()
                    + ";xiaaman;" + blankNine.getText().toString() + ";xiaaman;" + blankTen.getText().toString());
        }
    }

//    打开文件选择器
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes = {DOC, DOCX};
        intent.setType("*/*");

        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String path = getPath(this, uri);
                String url = uri.getPath();
                Log.d("文件路径", path);
                int index = url.indexOf("/",1);
                file = new File(path);

//                Log.d("文件", ""+uri.getPath());
//                Log.d("文件", ""+file.exists());
//                Log.d("文件", file.getAbsolutePath());
//                Log.d("文件", file.getName());
//                Log.d("文件", ""+file.getTotalSpace());
//                Log.d("文件", ""+file.length());
                long length = file.getTotalSpace();

                fileName.setHint(file.getName());
                tempFileName = file.getName();
                Log.d("蓝牙", "文件名称1 ："+ fileName.getHint().toString());
                chooseFilePath = uri.getPath();
            }
        }
    }

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it  Or Log it.
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public void uploadFile() {

//        submitExercise();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                ResponseBody responseBody = UploadUtil.getInstance().upload(Constant.URL_UPLOAD_FILE,file);
                if("200".equals(responseBody.string())) {
//                    fileName.setHint( "上传成功");
//                    fileName.setText("");
                    showResponse();


                } else {
                    fileName.setHint( "上传失败！");
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //上传文件成功后提交题目
    //提交答案
    public void submitFileExercise() {
        currentExercise.setExerciseCode("上传成功；"+ tempFileName);
        try {
            //为了方便传参，只好把用户名存入checkName
            submitUnable();
            UnionData unionData = new UnionData();
            currentExercise.setExerciseCheckUserName(new SysUserDb().getUserName());
            unionData.setCollectionId(collectionId);
            unionData.setExercise(currentExercise);
            String out = new OkhttpSubmitUtil(){}.execute(unionData).get();
            checkDecidedExerciseNumber();
//            submitOUtCome.setHint(out);
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    //execute the task
//                    displaySubmitOUtCome();
                    submitAble();
                }
            }, 500);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                submitFileExercise();
                checkDecidedExerciseNumber();
                fileName.setHint( "上传成功");
                fileName.setText("");
                Toast.makeText(ExerciseDetailActivity.this, "上传成功", Toast.LENGTH_LONG).show();
            }
        });
    }

    //提交答案
    public void submitExercise() {
        try {
            //为了方便传参，只好把用户名存入checkName
            submitUnable();
            UnionData unionData = new UnionData();
            currentExercise.setExerciseCheckUserName(new SysUserDb().getUserName());
            unionData.setCollectionId(collectionId);
            unionData.setExercise(currentExercise);
            String out = new OkhttpSubmitUtil(){}.execute(unionData).get();
            checkDecidedExerciseNumber();
            submitOUtCome.setHint(out);
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    //execute the task
                    displaySubmitOUtCome();
                    submitAble();
                }
            }, 500);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //在线编译
    public void onlineExercise() {
        try {
            //为了方便传参，只好把用户名存入checkName
            submitUnable();
            currentExercise.setExerciseCheckUserName(new SysUserDb().getUserName());
            String out = new OkhttpTestUtil(){}.execute(currentExercise).get();
            submitOUtCome.setHint(out);
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    //execute the task
                    displaySubmitOUtCome();
                    submitAble();
                }
            }, 500);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //设置提交按钮和在线运行按钮不可点击
    public void submitUnable() {
//        submitExercise.setBackgroundColor(getResources().getColor(R.color.disable));
//        submitExercise.setTextColor(getResources().getColor(R.color.gray));
//        testExercise.setBackgroundColor(getResources().getColor(R.color.disable));
//        testExercise.setTextColor(getResources().getColor(R.color.gray));
        submitExercise.setEnabled(false);
        testExercise.setEnabled(false);
    }

    //设置提交按钮和在线运行按钮可点击
    public void submitAble() {
//        submitExercise.setBackground(getDrawable(R.drawable.submit_btn_state));
//        testExercise.setBackground(getDrawable(R.drawable.submit_btn_state));
//        submitExercise.setTextColor(getResources().getColor(R.color.white));
//        testExercise.setTextColor(getResources().getColor(R.color.white));
//        submitExercise.setPadding(15,8,15,8);
//        testExercise.setPadding(15,8,15,8);
//        testExercise.setTextSize(18);
//        submitExercise.setTextSize(18);

        submitExercise.setEnabled(true);
        testExercise.setEnabled(true);
    }

    //判断填空题是否有空
    public boolean isHasBlank() {
//        int index = 0;
//        for(int i=0; i<blankList.size(); i++) {
//            if(blankList.get(i).getExerciseId().equals(currentExercise.getExerciseId())) {
//                index = i;
//                break;
//            }
//        }
        int number = currentExercise.getExerciseClassifyCause();
        if(number == 1) {
            if(blankOne.getText().length() == 0) {
                return false;
            }
        }
        if(number == 2) {
            if(blankOne.getText().length()==0 || blankTwo.getText().length() == 0) {
                return false;
            }
        }
        if(number == 3) {
            if(blankOne.getText().length()==0 || blankTwo.getText().length() == 0 || blankThree.getText().length() == 0) {
                return false;
            }
        }
        if(number == 4) {
            if(blankOne.getText().length()==0 || blankTwo.getText().length() == 0 || blankThree.getText().length() == 0
                    || blankFour.getText().length() == 0) {
                return false;
            }
        }
        if(number == 5) {
            if(blankOne.getText().length()==0 || blankTwo.getText().length() == 0 || blankThree.getText().length() == 0
                    || blankFour.getText().length() == 0 || blankFive.getText().length() == 0) {
                return false;
            }
        }
        if(number == 6) {
            if(blankOne.getText().length()==0 || blankTwo.getText().length() == 0 || blankThree.getText().length() == 0
                    || blankFour.getText().length() == 0 || blankFive.getText().length() == 0 || blankSix.getText().length() == 0) {
                return false;
            }
        }
        if(number == 7) {
            if(blankOne.getText().length()==0 || blankTwo.getText().length() == 0 || blankThree.getText().length() == 0
                    || blankFour.getText().length() == 0 || blankFive.getText().length() == 0 || blankSix.getText().length() == 0
                    || blankSeven.getText().length() == 0) {
                return false;
            }
        }
        if(number == 8) {
            if(blankOne.getText().length()==0 || blankTwo.getText().length() == 0 || blankThree.getText().length() == 0
                    || blankFour.getText().length() == 0 || blankFive.getText().length() == 0 || blankSix.getText().length() == 0
                    || blankSeven.getText().length() == 0 || blankEight.getText().length() == 0) {
                return false;
            }
        }
        if(number == 9) {
            if(blankOne.getText().length()==0 || blankTwo.getText().length() == 0 || blankThree.getText().length() == 0
                    || blankFour.getText().length() == 0 || blankFive.getText().length() == 0 || blankSix.getText().length() == 0
                    || blankSeven.getText().length() == 0 || blankEight.getText().length() == 0 || blankNine.getText().length() == 0) {
                return false;
            }
        }
        if(number == 10) {
            if(blankOne.getText().length()==0 || blankTwo.getText().length() == 0 || blankThree.getText().length() == 0
                    || blankFour.getText().length() == 0 || blankFive.getText().length() == 0 || blankSix.getText().length() == 0
                    || blankSeven.getText().length() == 0 || blankEight.getText().length() == 0 || blankNine.getText().length() == 0
                    || blankTen.getText().length() == 0) {
                return false;
            }
        }
        return  true;
    }

    //检测题目提交个数
    public void checkDecidedExerciseNumber() {
        int total=0,number=0, index=0;
        total = exerciseList.size();
        for(int i=0; i<tempExerciseList.size(); i++) {
            if(currentExercise.getExerciseId().equals(tempExerciseList.get(i).getExerciseId())) {
                index = i;
            }
        }
        tempExerciseList.get(index).setExerciseCode(exerciseList.get(index).getExerciseCode());
        for(int i=0; i<tempExerciseList.size(); i++) {
            if(tempExerciseList.get(i).getExerciseCode().length() > 0) {
                number++;
            }
        }
        decidedExercise.setText("答题信息（已提交/总题数）："+number+"题/"+total+"题");
    }


    private String getHTMLStr(String content) {

            content = content.replace("\n","<br>");

        return content;
    }
}