package com.example.rohan.f7.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rohan.f7.Choices;
import com.example.rohan.f7.ClassDetail;
import com.example.rohan.f7.R;
import com.example.rohan.f7.RecyclerAdapter;
import com.example.rohan.f7.SQLite;
import com.example.rohan.f7.SubjectDetails;
import com.example.rohan.f7.TinyDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Fri extends Fragment {
    FirebaseDatabase firebaseDatabase;
    List<ClassDetail> classDetails;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    List<ClassDetail> offline = new ArrayList<>();
    SQLite sqLite;
    private RecyclerAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fri, container, false);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        sqLite = new SQLite(getContext());
        recyclerView = view.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TinyDB tinyDB = new TinyDB(getActivity());
        try{
            if (tinyDB.getSubjectDetails("SEMESTER_5").get(4)!=null){

                ArrayList<SubjectDetails> subjectDetails = new ArrayList<>();
                if (tinyDB.getChoices("ELECTIVES")!=null)
                {
                    for (int i=0;i<tinyDB.getSubjectDetails("SEMESTER_5").get(4).size();i++){
                        if (tinyDB.getSubjectDetails("SEMESTER_5").get(4).get(i).getBatchName().contains(tinyDB.getString("BATCH")))
                        {
                            if (tinyDB.getSubjectDetails("SEMESTER_5").get(4).get(i).getSubjectValue().equals("CORE"))
                            {
                                subjectDetails.add(tinyDB.getSubjectDetails("SEMESTER_5").get(4).get(i));
                            }else{
                                Choices choices = new Choices();
                                choices = tinyDB.getChoices("ELECTIVES");
                                if (tinyDB.getSubjectDetails("SEMESTER_5").get(4).get(i).getSubjectName().contains(choices.getElective1())
                                        || tinyDB.getSubjectDetails("SEMESTER_5").get(4).get(i).getSubjectName().contains(choices.getElective2())
                                        || tinyDB.getSubjectDetails("SEMESTER_5").get(4).get(i).getSubjectName().contains(choices.getElective3())
                                        || tinyDB.getSubjectDetails("SEMESTER_5").get(4).get(i).getSubjectName().contains(choices.getElective4()))
                                {
                                    subjectDetails.add(tinyDB.getSubjectDetails("SEMESTER_5").get(4).get(i));
                                }
                            }
                        }


                    }
                    if (subjectDetails==null){
                        view.findViewById(R.id.noClassMsg).setVisibility(View.VISIBLE);
                    }

                    recyclerAdapter=new RecyclerAdapter(subjectDetails, getContext());
                    recyclerAdapter.notifyDataSetChanged();

                }else{
                    recyclerAdapter=new RecyclerAdapter(tinyDB.getSubjectDetails("SEMESTER_5").get(4), getContext());
                    recyclerAdapter.notifyDataSetChanged();

                }


                recyclerView.setAdapter(recyclerAdapter);
            }else{
                view.findViewById(R.id.noClassMsg).setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
            view.findViewById(R.id.noClassMsg).setVisibility(View.VISIBLE);
        }
        return view;
    }


}