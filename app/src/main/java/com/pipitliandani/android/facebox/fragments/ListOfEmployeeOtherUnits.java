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
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pipitliandani.android.facebox.BirthdayAdapter;
import com.pipitliandani.android.facebox.FaceBoxModel;
import com.pipitliandani.android.facebox.IKLAdapter;
import com.pipitliandani.android.facebox.R;
import com.pipitliandani.android.facebox.onLoadMore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfEmployeeOtherUnits.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListOfEmployeeOtherUnits#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOfEmployeeOtherUnits extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView rViewOtherUnits;
    DatabaseReference mDatabase;
    Query limit;
    IKLAdapter adapter;
    ArrayList<FaceBoxModel> list;
    String currentID;

    public ListOfEmployeeOtherUnits() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListOfEmployeeOtherUnits.
     */
    // TODO: Rename and change types and number of parameters
    public static ListOfEmployeeOtherUnits newInstance(String param1, String param2) {
        ListOfEmployeeOtherUnits fragment = new ListOfEmployeeOtherUnits();
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
        return inflater.inflate(R.layout.fragment_list_of_employee_other_units, container, false);
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
        Bundle bundle = this.getArguments();
        String title = bundle.getString("UNIT_NAME");
        String field = bundle.getString("KEY");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("employee");
        limit = mDatabase.orderByChild(field).equalTo(true);
        calculateTotal(limit);

        mDatabase.keepSynced(true);
        limit.keepSynced(true);
        list = new ArrayList<>();

        rViewOtherUnits = (RecyclerView) getView().findViewById(R.id.rViewOtherUnits);
        rViewOtherUnits.setHasFixedSize(true);
        rViewOtherUnits.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new IKLAdapter(getContext(), list, rViewOtherUnits, bundle.getString("TYPE"));
//        if(bundle == null) {
        adapter.setLoadMore(new onLoadMore() {
            @Override
            public void LoadMore() {
                Query query = limit.orderByKey().startAt(currentID);
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (!dataSnapshot.getKey().equals(currentID)) {
                            FaceBoxModel currentModel = dataSnapshot.getValue(FaceBoxModel.class);
                            currentID = dataSnapshot.getKey();
                            currentModel.setKey(currentID);
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
//        }

        rViewOtherUnits.setAdapter(adapter);
        limit.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FaceBoxModel currentModel = dataSnapshot.getValue(FaceBoxModel.class);
                currentID = dataSnapshot.getKey();
                currentModel.setKey(currentID);
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

    public void calculateTotal(Query limit) {
        final TextView totalNumber = (TextView) getView().findViewById(R.id.totalNumber);

        if (limit == null) {
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    totalNumber.setText(dataSnapshot.getChildrenCount() + "");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            limit.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    totalNumber.setText(dataSnapshot.getChildrenCount() + "");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
