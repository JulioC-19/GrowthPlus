package com.GrowthPlus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.GrowthPlus.dataAccessLayer.Flashcard.FlashcardSchema;
import com.GrowthPlus.dataAccessLayer.Lesson.LessonSchema;
import com.GrowthPlus.dataAccessLayer.child.ChildSchema;
import com.GrowthPlus.fragment.CustomImage;
import com.GrowthPlus.roadMapActivity.RoadMapOne;
import com.GrowthPlus.utilities.ImageSrcIdentifier;

import io.realm.Realm;
import io.realm.RealmList;

public class Flashcard extends AppCompatActivity {

    private String dataBaseLessonId;
    private String childId;
    private ChildSchema child;
    private LessonSchema lesson;
    private Realm realm;
    private RealmList<FlashcardSchema> lessonFlashcards;
    private String image;
    private ImageSrcIdentifier imageSrcIdentifier;
    private com.GrowthPlus.customViews.Flashcard flashcardContainer;
    private Button nextFlashcard;
    private FlashcardSchema flashcard;
    private FragmentManager fragmentManager;
    private String childAnswer;
    private ColorStateList answerColor;
    private ColorStateList correctAnswerColor;
    private ColorStateList wrongAnswerColor;
    private ColorStateList resetColor;
    private final int TEXT_INPUT_ONLY = 1;
    private final int NUMBER_INPUT_ONLY = 2;
    int counter;
    final int MAX = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        init();

        String initFirstNumber, initFirstOperator, initSecondNumber, initSecondOperator, initAnswer;
        int size = lessonFlashcards.size();

        /*
        * Switch statement for first flashcard, we don't have an intro so we start at index 0
        * */
        String initCategory = lessonFlashcards.get(0).getCategory();
        flashcard = lessonFlashcards.get(0);
        nextFlashcard.setVisibility(View.INVISIBLE);
        switch (initCategory){
            case "customImage":{
                image = flashcard.getImage();
                initFirstNumber = flashcard.getFirstNumber();
                initAnswer = flashcard.getAnswer();
                flashcardContainer.setRawInputType(NUMBER_INPUT_ONLY);
                if (savedInstanceState == null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("image", image);
                    bundle.putString("firstNumber", initFirstNumber);
                    bundle.putString("answer", initAnswer);
                    bundle.putBoolean("isAnimationDone", false);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setReorderingAllowed(true);
                    transaction.replace(flashcardContainer.findViewById(R.id.frame_layout_flashcard).getId(), CustomImage.class, bundle);
                    transaction.commit();
                }

                /*
                * Handles the answer verification logic.
                * If child taps the flashcard the animate() method fires up and rotates the flashcard 360 degrees.
                * Then, the onAnimationEnd sets the corresponding logic after the animation is done.
                * Once animation is done, the boolean flag isAnimationDone is set to true.
                * With this fragment, the fragment clears out the images and replaces it with only tha answer.
                * Refer to figma for clarification.
                * */
                flashcardContainer.setOnClickListener(view -> {
                    childAnswer = flashcardContainer.getAnswer();
                    if(childAnswer.equals(initAnswer)){
                       answerColor = correctAnswerColor;
                    }
                    else {
                        answerColor = wrongAnswerColor;
                    }
                    flashcardContainer.animate().setDuration(500).rotationYBy(360f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            if (savedInstanceState == null) {
                                Bundle bundle = new Bundle();
                                bundle.putString("image", image);
                                bundle.putString("firstNumber", initFirstNumber);
                                bundle.putString("answer", initAnswer);
                                bundle.putBoolean("isAnimationDone", true);
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.setReorderingAllowed(true);
                                transaction.replace(flashcardContainer.findViewById(R.id.frame_layout_flashcard).getId(), CustomImage.class, bundle);
                                transaction.commit();
                            }

                            flashcardContainer.setFlashcardColor(answerColor);
                            flashcardContainer.setEnabled(false);
                            flashcardContainer.setAnswerEnabled(false);
                            nextFlashcard.setVisibility(View.VISIBLE);
                        }
                    });
                });

                break;
            }
            // More cases for different lessons
        }

        // Now handle the next flashcard onClick, increment the counter to go to next flashcard
        // Make sure to reset the flashcardContainer state
        nextFlashcard.setOnClickListener(view -> {
            counter++;
            flashcardContainer.setEnabled(true);
            flashcardContainer.setAnswerEnabled(true);
            if(counter >= MAX){
                Intent lessonIntent = new Intent(Flashcard.this, RoadMapOne.class); // TODO: Dynamically change location address
                lessonIntent.putExtra("childIdentify", childId);
                startActivity(lessonIntent);
                this.finish();
            }else {
                flashcardContainer.setText(null);
                flashcardContainer.setFlashcardColor(resetColor);
                String image, firstNumber, firstOperator, secondNumber, secondOperator, answer, category;
                flashcard = lessonFlashcards.get(counter);
                category = flashcard.getCategory();

                switch (category){
                    case "customImage":{
                        nextFlashcard.setVisibility(View.INVISIBLE);
                        image = flashcard.getImage();
                        firstNumber = flashcard.getFirstNumber();
                        answer = flashcard.getAnswer();
                        flashcardContainer.setRawInputType(NUMBER_INPUT_ONLY);
                        if (savedInstanceState == null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("image", image);
                            bundle.putString("firstNumber", firstNumber);
                            bundle.putString("answer", answer);
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.setReorderingAllowed(true);
                            transaction.replace(flashcardContainer.findViewById(R.id.frame_layout_flashcard).getId(), CustomImage.class, bundle);
                            transaction.commit();
                        }

                        /*
                         * Handles the answer verification logic, if child taps the flashcard
                         * the animate() method fires up and rotates the flashcard.
                         * Then, the onAnimationEnd sets the corresponding color.
                         * After animation is done, disable the flashcard and next button is visible again.
                         * */
                        flashcardContainer.setOnClickListener(view1 -> {
                            childAnswer = flashcardContainer.getAnswer();
                            if(childAnswer.equals(answer)){
                                answerColor = correctAnswerColor;
                            }
                            else {
                                answerColor = wrongAnswerColor;
                            }
                            flashcardContainer.animate().setDuration(500).rotationYBy(360f).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);

                                    if (savedInstanceState == null) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("image", image);
                                        bundle.putString("firstNumber", firstNumber);
                                        bundle.putString("answer", answer);
                                        bundle.putBoolean("isAnimationDone", true);
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        transaction.setReorderingAllowed(true);
                                        transaction.replace(flashcardContainer.findViewById(R.id.frame_layout_flashcard).getId(), CustomImage.class, bundle);
                                        transaction.commit();
                                    }

                                    flashcardContainer.setFlashcardColor(answerColor);
                                    flashcardContainer.setEnabled(false);
                                    flashcardContainer.setAnswerEnabled(false);
                                    nextFlashcard.setVisibility(View.VISIBLE);
                                }
                            });
                        });

                        break;
                    }
                }
            }
        });




    }

    private void init(){
        realm = Realm.getDefaultInstance();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            dataBaseLessonId = extras.getString("dataBaseLessonId");
            childId = extras.getString("childId");
            image = extras.getString("lessonImage");
        }
        imageSrcIdentifier = new ImageSrcIdentifier();
        child = realm.where(ChildSchema.class).equalTo("childId", childId).findFirst();
        lesson = realm.where(LessonSchema.class).equalTo("lessonId", dataBaseLessonId).findFirst();
        assert lesson != null;
        lessonFlashcards = lesson.getFlashcards();
        flashcardContainer = findViewById(R.id.flashcardContainer);
        nextFlashcard = findViewById(R.id.next_button_flashcard);
        fragmentManager = getSupportFragmentManager();
        correctAnswerColor = ContextCompat.getColorStateList(this, R.color.light_green);
        wrongAnswerColor = ContextCompat.getColorStateList(this, R.color.red);
        resetColor = ContextCompat.getColorStateList(this, R.color.blue);

    }
}