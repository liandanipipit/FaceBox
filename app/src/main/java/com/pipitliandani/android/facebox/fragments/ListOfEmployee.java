package com.pipitliandani.android.facebox.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pipitliandani.android.facebox.FaceBoxModel;
import com.pipitliandani.android.facebox.ListAdapter;
import com.pipitliandani.android.facebox.R;
import com.pipitliandani.android.facebox.onLoadMore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfEmployee.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListOfEmployee#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOfEmployee extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rViewmain;
    DatabaseReference mDatabase;
    Query limit;
    ListAdapter adapter;
    ArrayList<FaceBoxModel> list;
    String currentID;

    private OnFragmentInteractionListener mListener;

    public ListOfEmployee() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListOfEmployee.
     */
    // TODO: Rename and change types and number of parameters
    public static ListOfEmployee newInstance(String param1, String param2) {
        ListOfEmployee fragment = new ListOfEmployee();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_of_employee, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("List Of Employee");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("employee");
        limit = mDatabase.limitToFirst(10).orderByKey();
        Bundle bundle = this.getArguments();
        if (bundle != null){
            if(bundle.getString("UNIT_KEY") != null){
                limit = mDatabase.orderByChild("unit").equalTo(bundle.getString("UNIT_KEY"));
            } else {
                limit = mDatabase.orderByChild(bundle.getString("NIK_KEY")).equalTo(true);
            }
        }


        limit.keepSynced(true);
        mDatabase.keepSynced(true);
        list = new ArrayList<>();

        rViewmain = (RecyclerView) getView().findViewById(R.id.rViewMain);
        rViewmain.setHasFixedSize(true);
        rViewmain.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ListAdapter(getContext(), list, rViewmain);
        if(bundle == null) {
            adapter.setLoadMore(new onLoadMore() {
                @Override
                public void LoadMore() {
                    Query query = mDatabase.orderByKey().startAt(currentID).limitToFirst(10);
                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if ( !dataSnapshot.getKey().equals(currentID)) {
                                FaceBoxModel currentModel = dataSnapshot.getValue(FaceBoxModel.class);
                                currentID = dataSnapshot.getKey();
                                list.add(currentModel);
                                adapter.notifyDataSetChanged();
                                adapter.setLoaded();
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

        rViewmain.setAdapter(adapter);
        limit.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FaceBoxModel currentModel = dataSnapshot.getValue(FaceBoxModel.class);
                currentID = dataSnapshot.getKey();
                list.add(currentModel);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
