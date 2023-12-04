package com.sprtcoding.obslearn.FireStoreDB;

import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sprtcoding.obslearn.Loadings.LoadingDialog;
import com.sprtcoding.obslearn.Model.CatListModel;
import com.sprtcoding.obslearn.Model.QuestionModel;
import com.sprtcoding.obslearn.Model.TestCatModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class DBQuery {
    public static FirebaseFirestore g_firestore;
    public static List<CatListModel> g_catListModel = new ArrayList<>();
    public static int g_selected_cat_index = 0;
    public static List<TestCatModel> g_testList = new ArrayList<>();
    public static int g_selected_test_index = 0;
    public static List<QuestionModel> g_questList = new ArrayList<>();
    public static final int NOT_VISITED = 0;
    public static final int UNANSWERED = 1;
    public static final int ANSWERED = 2;
    public static final int REVIEW = 3;
    public static void createUserData(String email, String name, String id, String accountType, String dob, int age,
                                      String gender, MyCompleteListener myCompleteListener) {
        Map<String, Object> userData = new HashMap<>();

        userData.put("USER_ID", id);
        userData.put("EMAIL_ID", email);
        userData.put("NAME", name);
        userData.put("ACCOUNT_TYPE", accountType);
        userData.put("DATE_OF_BIRTH", dob);
        userData.put("AGE", age);
        userData.put("GENDER", gender);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userDoc.update(userData)
                .addOnSuccessListener(unused -> {
                    myCompleteListener.onSuccess();
                }).addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void setUserData(String email, String name, String id, String accountType, String dob, int age,
                                      String gender, MyCompleteListener myCompleteListener) {
        Map<String, Object> userData = new HashMap<>();

        userData.put("USER_ID", id);
        userData.put("EMAIL_ID", email);
        userData.put("NAME", name);
        userData.put("ACCOUNT_TYPE", accountType);
        userData.put("DATE_OF_BIRTH", dob);
        userData.put("AGE", age);
        userData.put("GENDER", gender);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userDoc.set(userData)
                .addOnSuccessListener(unused -> {
                    myCompleteListener.onSuccess();
                }).addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void addUserCount() {
        DocumentReference countDoc = g_firestore.collection("USERS").document("TOTAL_USERS");
        countDoc.update("COUNT", FieldValue.increment(1));
    }

    public static void updateUsers(String dob, int age, String gender, MyCompleteListener myCompleteListener) {
        Map<String, Object> userData = new HashMap<>();

        userData.put("DATE_OF_BIRTH", dob);
        userData.put("AGE", age);
        userData.put("GENDER", gender);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userDoc.update(userData).addOnSuccessListener(unused -> {
            myCompleteListener.onSuccess();
        }).addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void updateName(String name, MyCompleteListener myCompleteListener) {
        Map<String, Object> userData = new HashMap<>();

        userData.put("NAME", name);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userDoc.update(userData).addOnSuccessListener(unused -> {
            myCompleteListener.onSuccess();
        }).addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void updateEmail(String email, MyCompleteListener myCompleteListener) {
        Map<String, Object> userData = new HashMap<>();

        userData.put("EMAIL_ID", email);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userDoc.update(userData).addOnSuccessListener(unused -> {
            myCompleteListener.onSuccess();
        }).addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void updateAge(int age, MyCompleteListener myCompleteListener) {
        Map<String, Object> userData = new HashMap<>();

        userData.put("AGE", age);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userDoc.update(userData).addOnSuccessListener(unused -> {
            myCompleteListener.onSuccess();
        }).addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void updateGender(String txt, MyCompleteListener myCompleteListener) {
        Map<String, Object> userData = new HashMap<>();

        userData.put("GENDER", txt);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userDoc.update(userData).addOnSuccessListener(unused -> {
            myCompleteListener.onSuccess();
        }).addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void updateDateOfBirth(String txt, MyCompleteListener myCompleteListener) {
        Map<String, Object> userData = new HashMap<>();

        userData.put("DATE_OF_BIRTH", txt);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userDoc.update(userData).addOnSuccessListener(unused -> {
            myCompleteListener.onSuccess();
        }).addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void setTest(String testID, String testType, String topics, String question, String c1, String c2, String c3, String c4, String correctAnswer, String time, String date, MyCompleteListener myCompleteListener) {
        Map<String, Object> test = new HashMap<>();
        test.put("TEST_ID", testID);
        test.put("TEST_TYPE", testType);
        test.put("TOPICS", topics);
        test.put("QUESTION", question);
        test.put("C1", c1);
        test.put("C2", c2);
        test.put("C3", c3);
        test.put("C4", c4);
        test.put("CORRECT_ANSWER", correctAnswer);
        test.put("TIME", time);
        test.put("DATE", date);

        DocumentReference testDoc = g_firestore.collection("EXAMINATION").document(testID);

        testDoc.set(test).addOnSuccessListener(unused -> {
            myCompleteListener.onSuccess();
        }).addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void removePrePostTest(String id, MyCompleteListener myCompleteListener) {
        Task<Void> testDoc = g_firestore.collection("EXAMINATION").document(id).delete();
        testDoc.addOnSuccessListener(unused -> {
            myCompleteListener.onSuccess();
        }).addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void loadCategories(MyCompleteListener myCompleteListener) {
        g_catListModel.clear();

        g_firestore.collection("EXAMINATION")
                .addSnapshotListener((value, error) -> {
                    if(error == null && value != null) {
                        if(!value.isEmpty()) {
                            Map<String, QueryDocumentSnapshot> map = new ArrayMap<>();

                            for(QueryDocumentSnapshot doc : value) {
                                map.put(doc.getId(), doc);
                            }

                            QueryDocumentSnapshot catListDoc = map.get("Categories");

                            assert catListDoc != null;
                            long catCount = catListDoc.getLong("COUNT");

                            for(int i = 1; i <= catCount; i++) {
                                String catID = catListDoc.getString("CAT"+ i +"_ID");
                                QueryDocumentSnapshot catDoc = map.get(catID);

                                assert catDoc != null;
                                int noOfTest = Objects.requireNonNull(catDoc.getLong("NO_OF_TEST")).intValue();
                                String catName = catDoc.getString("NAME");
                                g_catListModel.add(new CatListModel(catID, catName, noOfTest));
                            }
                            myCompleteListener.onSuccess();
                        }
                    } else {
                        myCompleteListener.onFailure(error);
                    }
                });
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    if(!queryDocumentSnapshots.isEmpty()) {
//                        Map<String, QueryDocumentSnapshot> map = new ArrayMap<>();
//
//                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                            map.put(doc.getId(), doc);
//                        }
//
//                        QueryDocumentSnapshot catListDoc = map.get("Categories");
//
//                        assert catListDoc != null;
//                        long catCount = catListDoc.getLong("COUNT");
//
//                        for(int i = 1; i <= catCount; i++) {
//                            String catID = catListDoc.getString("CAT"+ i +"_ID");
//                            QueryDocumentSnapshot catDoc = map.get(catID);
//
//                            assert catDoc != null;
//                            int noOfTest = Objects.requireNonNull(catDoc.getLong("NO_OF_TEST")).intValue();
//                            String catName = catDoc.getString("NAME");
//                            g_catListModel.add(new CatListModel(catID, catName, noOfTest));
//                        }
//                        myCompleteListener.onSuccess();
//                    }
//                }).addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void deleteTest(String catID, String testIDField, String testTimeField, String testID, MyCompleteListener myCompleteListener) {
        DocumentReference testDoc = g_firestore.collection("EXAMINATION")
                .document(catID)
                .collection("TESTS_LIST")
                .document("TESTS_INFO");

        testDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                Map<String, Object> testData = new HashMap<>();
                if (document != null && document.exists()) {

                    int noOfTest = g_catListModel.get(g_selected_cat_index).getNO_OF_TEST();
                    boolean testFound = false;

                    if (document.contains(testIDField) && Objects.equals(document.getString(testIDField), testID)) {
                        // Delete the test by setting its fields to null
                        testData.put(testIDField, FieldValue.delete());
                        testData.put(testTimeField, FieldValue.delete());

                        //testDoc.update(testData);

                        int newNoOfTest = noOfTest - 1;

                        if (newNoOfTest > 0) {
                            Map<String, Object> testData1 = new HashMap<>();

                            // Iterate over remaining test fields and update their names
                            for (int i = 1; i <= newNoOfTest; i++) {

                                // Update field names to fill the gap

                                String currentTestID = document.getString("TEST" + (i + 1) + "_ID");
                                int currentTestTime = document.getLong("TEST" + (i + 1) + "_TIME").intValue();

                                testData.put("TEST" + i + "_ID", currentTestID);
                                testData.put("TEST" + i + "_TIME", currentTestTime);

//                                if(document.getString("TEST" + (i + 1) + "_ID") != null && document.getLong("TEST" + (i + 1) + "_TIME") != null) {
//                                    String currentTestID = document.getString("TEST" + (i + 1) + "_ID");
//                                    int currentTestTime = document.getLong("TEST" + (i + 1) + "_TIME").intValue();
//
//                                    testData.put("TEST" + i + "_ID", currentTestID);
//                                    testData.put("TEST" + i + "_TIME", currentTestTime);
//
//                                    Log.d("dataaaa", testData1.toString());
//                                }else {
//                                    String currentTestID1 = document.getString("TEST" + (i + 2) + "_ID");
//                                    int currentTestTime1 = document.getLong("TEST" + (i + 2) + "_TIME").intValue();
//
//                                    testData1.put("TEST" + i + "_ID", currentTestID1);
//                                    testData1.put("TEST" + i + "_TIME", currentTestTime1);
//
//                                    Log.d("dataaaa", testData1.toString());
//                                }

                            }

                            int duplicate = newNoOfTest + 1;
                            // Remove the last test field, as it is now duplicated
                            testData.put("TEST" + duplicate + "_ID", FieldValue.delete());
                            testData.put("TEST" + duplicate + "_TIME", FieldValue.delete());

                            // If there are still tests, update the NO_OF_TEST in the "EXAMINATION" document
                            g_firestore.collection("EXAMINATION")
                                    .document(catID)
                                    .update("NO_OF_TEST", newNoOfTest)
                                    .addOnSuccessListener(unused -> {
                                        // Update the Firestore document to delete the test and adjust field names
                                        testDoc.update(testData)
                                                .addOnSuccessListener(unused1 -> myCompleteListener.onSuccess())
                                                .addOnFailureListener(myCompleteListener::onFailure);
                                    })
                                    .addOnFailureListener(myCompleteListener::onFailure);

                        } else {
                            // If the number of tests is zero, delete the "TESTS_LIST" collection

                            g_firestore.collection("EXAMINATION")
                                    .document(catID)
                                    .update("NO_OF_TEST", newNoOfTest)
                                    .addOnSuccessListener(unused -> {
                                        g_firestore.collection("EXAMINATION")
                                                .document(catID)
                                                .collection("TESTS_LIST")
                                                .document("TESTS_INFO")
                                                .delete()
                                                .addOnSuccessListener(unused1 -> myCompleteListener.onSuccess())
                                                .addOnFailureListener(myCompleteListener::onFailure);
                                    })
                                    .addOnFailureListener(myCompleteListener::onFailure);
                        }

                        testFound = true;
                    }

                    if (!testFound) {
                        // Test with the specified testID was not found
                        myCompleteListener.onFailure(new Exception("Test not found."));
                    }
                } else {
                    myCompleteListener.onFailure(task.getException());
                }
            } else {
                myCompleteListener.onFailure(task.getException());
            }
        });
    }

    public static void updateTestName(String catID, String testIDField, String testTimeField, String oldName, String newName, int testTIME, MyCompleteListener myCompleteListener) {
        DocumentReference testDoc = g_firestore.collection("EXAMINATION")
                .document(catID)
                .collection("TESTS_LIST")
                .document("TESTS_INFO");

        CollectionReference questionDoc = g_firestore.collection("QUESTIONS");

        testDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                Map<String, Object> testData = new HashMap<>();
                if (document != null && document.exists()) {

                    if (document.contains(testIDField) && Objects.equals(document.getString(testIDField), oldName)) {
                        // Update the name of the test
                        testData.put(testIDField, newName);
                        testData.put(testTimeField, testTIME);

                        questionDoc
                                .whereEqualTo("TEST", oldName)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                        String docId = doc.getId();
                                        questionDoc.document(docId).update("TEST", newName);
                                    }
                                    myCompleteListener.onSuccess();
                                })
                                .addOnFailureListener(e -> {
                                    myCompleteListener.onFailure(new Exception("Questions not found."));
                                });
                    }

                    if (!testData.isEmpty()) {
                        // Update the Firestore document with the new test name
                        testDoc.update(testData)
                                .addOnSuccessListener(unused -> myCompleteListener.onSuccess())
                                .addOnFailureListener(myCompleteListener::onFailure);
                    } else {
                        // Test with the specified testID was not found
                        myCompleteListener.onFailure(new Exception("Test not found."));
                    }
                } else {
                    myCompleteListener.onFailure(task.getException());
                }
            } else {
                myCompleteListener.onFailure(task.getException());
            }
        });
    }

    public static void setTestInfo(String catID,
                                   String testID,
                                   int testTIME,
                                   MyCompleteListener myCompleteListener) {
        DocumentReference testDoc = g_firestore.collection("EXAMINATION")
                .document(catID)
                .collection("TESTS_LIST")
                .document("TESTS_INFO");

        testDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                Map<String, Object> testData = new HashMap<>();
                if (document != null && document.exists()) {

                    int lastTestNumber = getLastTestNumber(document);
                    int lastTestTime = getLastTestTime(document);

                    // Construct the field names for the new test
                    String nextTestIDField = "TEST" + (lastTestNumber + 1) + "_ID";
                    String nextTestTimeField = "TEST" + (lastTestTime + 1) + "_TIME";

                    // Add the new test ID and time to the map
                    testData.put(nextTestIDField, testID);
                    testData.put(nextTestTimeField, testTIME);

                    // Update the Firestore document with the new or modified test IDs
                    testDoc.update(testData)
                            .addOnSuccessListener(unused -> {
                                DocumentReference testDocUpdate = g_firestore.collection("EXAMINATION")
                                        .document(catID);

                                Map<String, Object> map = new ArrayMap<>();
                                map.put("NO_OF_TEST", lastTestNumber + 1);

                                testDocUpdate.update(map)
                                        .addOnSuccessListener(unused1 -> myCompleteListener.onSuccess())
                                        .addOnFailureListener(myCompleteListener::onFailure);
                            })
                            .addOnFailureListener(myCompleteListener::onFailure);
                } else {

                    // Construct the field names for the new test
                    String nextTestIDField = "TEST1_ID";
                    String nextTestTimeField = "TEST1_TIME";

                    // Add the new test ID and time to the map
                    testData.put(nextTestIDField, testID);
                    testData.put(nextTestTimeField, testTIME);

                    // Update the Firestore document with the new or modified test IDs
                    testDoc.set(testData)
                            .addOnSuccessListener(unused -> {
                                DocumentReference testDocUpdate = g_firestore.collection("EXAMINATION")
                                        .document(catID);

                                Map<String, Object> map = new ArrayMap<>();
                                map.put("NO_OF_TEST", 1);

                                testDocUpdate.update(map)
                                        .addOnSuccessListener(unused1 -> myCompleteListener.onSuccess())
                                        .addOnFailureListener(myCompleteListener::onFailure);
                            })
                            .addOnFailureListener(myCompleteListener::onFailure);
                }
            } else {
                myCompleteListener.onFailure(task.getException());
            }
        });
    }

    private static int getLastTestTime(DocumentSnapshot document) {
        int lastTestNumber = 0;
        int noOfTest = g_catListModel.get(g_selected_cat_index).getNO_OF_TEST();

        if(noOfTest > 0) {
            for (int i = 1; i <= noOfTest; i++) {
                String testIDField = "TEST" + i + "_TIME";
                if (document.contains(testIDField)) {
                    lastTestNumber = i;
                } else {
                    // If a field is missing in sequence, break the loop
                    lastTestNumber = 1;
                    break;
                }
            }
        }else {
            lastTestNumber = 1;
        }

        return lastTestNumber;
    }

    private static int getLastTestNumber(DocumentSnapshot document) {
        int lastTestNumber = 0;
        int noOfTest = g_catListModel.get(g_selected_cat_index).getNO_OF_TEST();

        if(noOfTest > 0) {
            for (int i = 1; i <= noOfTest; i++) {
                String testIDField = "TEST" + i + "_ID";
                if (document.contains(testIDField)) {
                    lastTestNumber = i;
                } else {
                    // If a field is missing in sequence, break the loop
                    lastTestNumber = 1;
                    break;
                }
            }
        }else {
            lastTestNumber = 1;
        }

        return lastTestNumber;
    }

    public static void loadTestData(MyCompleteListener myCompleteListener, LinearLayout no_data, RecyclerView rv, LoadingDialog loadingDialog) {
        g_testList.clear();

        g_firestore.collection("EXAMINATION").document(g_catListModel.get(g_selected_cat_index).getCAT_ID())
                .collection("TESTS_LIST")
                .document("TESTS_INFO")
//                .addSnapshotListener((value, error) -> {
//                    if(error == null && value != null) {
//                        if(value.exists()) {
//                            int noOfTest = g_catListModel.get(g_selected_cat_index).getNO_OF_TEST();
//
//                            if(noOfTest > 0) {
//                                for(int i = 1; i <= noOfTest; i++) {
//                                    g_testList.add(new TestCatModel(
//                                            value.getString("TEST" + i + "_ID"),
//                                            "TEST" + i + "_ID",
//                                            "TEST" + i + "_TIME",
//                                            0,
//                                            value.getLong("TEST" + i + "_TIME").intValue()
//                                    ));
//                                }
//                            }else {
//                                g_testList.add(new TestCatModel(
//                                        value.getString("TEST" + 1 + "_ID"),
//                                        "TEST" + 1 + "_ID",
//                                        "TEST" + 1 + "_TIME",
//                                        0,
//                                        value.getLong("TEST" + 1 + "_TIME").intValue()
//                                ));
//                            }
//
//                            myCompleteListener.onSuccess();
//                        }else {
//                            loadingDialog.dismiss();
//                            no_data.setVisibility(View.VISIBLE);
//                            rv.setVisibility(View.GONE);
//                        }
//                    }else {
//                        myCompleteListener.onFailure(error);
//                    }
//                });
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        // Add log statements to see if the listener is triggered
                        Log.d("FirestoreListener", "Document changed: " + documentSnapshot);
                        int noOfTest = g_catListModel.get(g_selected_cat_index).getNO_OF_TEST();

                        if(noOfTest > 0) {
                            for(int i = 1; i <= noOfTest; i++) {
                                g_testList.add(new TestCatModel(
                                        documentSnapshot.getString("TEST" + i + "_ID"),
                                        "TEST" + i + "_ID",
                                        "TEST" + i + "_TIME",
                                        0,
                                        documentSnapshot.getLong("TEST" + i + "_TIME").intValue()
                                ));
                            }
                        }else {
                            g_testList.add(new TestCatModel(
                                    documentSnapshot.getString("TEST" + 1 + "_ID"),
                                    "TEST" + 1 + "_ID",
                                    "TEST" + 1 + "_TIME",
                                    0,
                                    documentSnapshot.getLong("TEST" + 1 + "_TIME").intValue()
                            ));
                        }

                        myCompleteListener.onSuccess();
                    } else {
                        loadingDialog.dismiss();
                        no_data.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    myCompleteListener.onFailure(e);
                });
    }

    public static void loadQuestions(MyCompleteListener myCompleteListener, LoadingDialog loadingDialog, LinearLayout no_data, LinearLayout with_data, String testID) {
        g_questList.clear();
        g_firestore.collection("QUESTIONS")
                .whereEqualTo("CATEGORY", g_catListModel.get(g_selected_cat_index).getCAT_ID())
                .whereEqualTo("TEST", testID)
                .addSnapshotListener((value, error) -> {
                    if(error == null && value != null) {
                        if(!value.isEmpty()) {
                            for(QueryDocumentSnapshot doc : value) {
                                g_questList.add(new QuestionModel(
                                        doc.getString("QUESTION"),
                                        doc.getString("A"),
                                        doc.getString("B"),
                                        doc.getString("C"),
                                        doc.getString("D"),
                                        doc.getString("QUESTION_ID"),
                                        doc.getString("TEST"),
                                        doc.getLong("ANSWER").intValue(),
                                        -1,
                                        NOT_VISITED
                                ));

                                myCompleteListener.onSuccess();
                            }
                        }else {
                            loadingDialog.dismiss();
                            no_data.setVisibility(View.VISIBLE);
                            with_data.setVisibility(View.GONE);
                        }
                    }else {
                        myCompleteListener.onFailure(error);
                    }
                });
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    if(!queryDocumentSnapshots.isEmpty()) {
//                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                            g_questList.add(new QuestionModel(
//                                    doc.getString("QUESTION"),
//                                    doc.getString("A"),
//                                    doc.getString("B"),
//                                    doc.getString("C"),
//                                    doc.getString("D"),
//                                    doc.getString("QUESTION_ID"),
//                                    doc.getLong("ANSWER").intValue(),
//                                    -1,
//                                    NOT_VISITED
//                            ));
//
//                            myCompleteListener.onSuccess();
//                        }
//                    }else {
//                        loadingDialog.dismiss();
//                        no_data.setVisibility(View.VISIBLE);
//                        with_data.setVisibility(View.GONE);
//                    }
//                })
//                .addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void removeQuestions(MyCompleteListener myCompleteListener, String qID) {
        g_firestore.collection("QUESTIONS").document(qID)
                .delete()
                .addOnSuccessListener(unused -> myCompleteListener.onSuccess())
                .addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void setQuestions(String catID, String testID, String Question, String option1, String option2, String option3,
                                    String option4, int answer, MyCompleteListener myCompleteListener) {
        // Create a new collection reference
        CollectionReference collectionRef = g_firestore.collection("QUESTIONS");

        Map<String, Object> qData = new HashMap<>();

        qData.put("CATEGORY", catID);
        qData.put("TEST", testID);
        qData.put("QUESTION", Question);
        qData.put("A", option1);
        qData.put("B", option2);
        qData.put("C", option3);
        qData.put("D", option4);
        qData.put("ANSWER", answer);

        // Add a new document with an auto-generated ID and set the data
        collectionRef.add(qData)
                .addOnSuccessListener(documentReference -> {
                    String questionID = documentReference.getId();
                    Map<String, Object> IDMap = new HashMap<>();
                    IDMap.put("QUESTION_ID", questionID);
                    collectionRef.document(questionID).update(IDMap)
                            .addOnSuccessListener(unused -> myCompleteListener.onSuccess());
                })
                .addOnFailureListener(myCompleteListener::onFailure);
    }

    public static void saveResult(String id, int score, String numScore, MyCompleteListener myCompleteListener) {

        g_firestore.collection("EXAMINATION")
                .document(g_catListModel.get(g_selected_cat_index).getCAT_ID())
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    String catName = documentSnapshot.getString("NAME");

                    if(documentSnapshot.exists()) {
                        DocumentReference userDoc = g_firestore.collection("USERS")
                                .document(id).collection("MY_SCORE").document(g_testList.get(g_selected_test_index).getTestID() + "(" + catName + ")");

                        Map<String, Object> map = new HashMap<>();
                        map.put("TOTAL_SCORE", score);
                        map.put("TOTAL_SCORE_NOT_PERCENT", numScore);
                        map.put("ID", g_testList.get(g_selected_test_index).getTestID());

                        // Handle Firestore exception
                        userDoc.set(map).addOnSuccessListener(unused -> {
                                    if (score > g_testList.get(g_selected_test_index).getTopScore()) {
                                        g_testList.get(g_selected_test_index).setTopScore(score);
                                    }
                                    myCompleteListener.onSuccess();
                                })
                                .addOnFailureListener(myCompleteListener::onFailure);
                    }
                })
                .addOnFailureListener(myCompleteListener::onFailure);
    }
}
