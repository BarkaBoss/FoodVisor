package xyz.nokt.btf.foodadvisor;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class BlankFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    Bundle bundle =  new Bundle();
    public BlankFragment() {
        // Required empty public constructor
    }
    TextView tvMessage;
    Button btnYes, btnNo, btnNorthern, btnYoruba, btnIgbo, btnVegan, btnHalal, btnGluten, btnDiab,
            btnChinese, btnIndian, btnLebanese, btnContinental, btnfastFood;
    LinearLayout linearLayout, lineMain, lineDiet, linefore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //loadDialog();
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        tvMessage = rootView.findViewById(R.id.tvDiagMessage);
        btnYes = rootView.findViewById(R.id.btnYes);
        btnNo = rootView.findViewById(R.id.btnNo);

        btnNorthern = rootView.findViewById(R.id.arewa);
        btnYoruba = rootView.findViewById(R.id.yoruba);
        btnIgbo = rootView.findViewById(R.id.igbo);

        btnVegan = rootView.findViewById(R.id.vegan);
        btnHalal = rootView.findViewById(R.id.halal);
        btnGluten = rootView.findViewById(R.id.gluten);
        btnDiab = rootView.findViewById(R.id.diabetes);

        btnChinese = rootView.findViewById(R.id.chinese);
        btnIndian = rootView.findViewById(R.id.indian);
        btnLebanese = rootView.findViewById(R.id.lebanese);
        btnContinental = rootView.findViewById(R.id.continental);
        btnfastFood = rootView.findViewById(R.id.fastfoof);

        linearLayout = rootView.findViewById(R.id.local);
        lineMain = rootView.findViewById(R.id.linMain);
        lineDiet = rootView.findViewById(R.id.linediet);
        linefore = rootView.findViewById(R.id.lineforeign);

        if (lineMain.getVisibility() == View.GONE) {
            linearLayout.setVisibility(View.GONE);
            lineMain.setVisibility(View.VISIBLE);
            lineDiet.setVisibility(View.GONE);
            linefore.setVisibility(View.GONE);
            getActivity().setTitle("Restaurant Advisor");
            Log.i("Resumed", "Resumes");
        }
        Log.i("Resumed", "Resumes");


        //getActivity().setTitle("Welcome to Food Advisor");
        tvMessage.setText("Would you like to search for Restaurants based on your preference?");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDietDialog();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                ViewRestaurants viewRestaurants = new ViewRestaurants();
                ft.replace(R.id.frag_main, viewRestaurants).addToBackStack(null)
                        .commit();
            }
        });

        if(linearLayout.getVisibility() != View.GONE || linearLayout.getVisibility() != View.INVISIBLE)
        {
            //tvMessage.setText("Which is your preference?");
            btnNorthern.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("localForeign", "northern");
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                    autoRec.setArguments(bundle);
                    ft.replace(R.id.frag_main, autoRec).addToBackStack(null)
                            .commit();
                }
            });

            btnYoruba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("localForeign", "yoruba");
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                    autoRec.setArguments(bundle);
                    ft.replace(R.id.frag_main, autoRec).addToBackStack(null)
                            .commit();
                }
            });

            btnIgbo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("localForeign", "igbo");
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                    autoRec.setArguments(bundle);
                    ft.replace(R.id.frag_main, autoRec).addToBackStack(null)
                            .commit();
                }
            });
        }

        if(lineDiet.getVisibility() != View.GONE || lineDiet.getVisibility() != View.INVISIBLE)
        {
            //tvMessage.setText("Which is your preference?");
            btnVegan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("localForeign", "vegan");
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                    autoRec.setArguments(bundle);
                    ft.replace(R.id.frag_main, autoRec).addToBackStack(null)
                            .commit();
                }
            });
            btnGluten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("localForeign", "gluten");
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                    autoRec.setArguments(bundle);
                    ft.replace(R.id.frag_main, autoRec).addToBackStack(null)
                            .commit();
                }
            });
            btnHalal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("localForeign", "halal");
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                    autoRec.setArguments(bundle);
                    ft.replace(R.id.frag_main, autoRec).addToBackStack(null)
                            .commit();
                }
            });
            btnDiab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("localForeign", "diabetes");
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                    autoRec.setArguments(bundle);
                    ft.replace(R.id.frag_main, autoRec).addToBackStack(null)
                            .commit();
                }
            });
        }
        if(linefore.getVisibility() != View.GONE || linefore.getVisibility() != View.INVISIBLE)
        {
            //tvMessage.setText("Which is your preference?");
            btnChinese.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("localForeign", "chinese");
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                    autoRec.setArguments(bundle);
                    ft.replace(R.id.frag_main, autoRec).addToBackStack(null)
                            .commit();
                }
            });
            btnLebanese.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("localForeign", "lebanese");
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                    autoRec.setArguments(bundle);
                    ft.replace(R.id.frag_main, autoRec).addToBackStack(null)
                            .commit();
                }
            });
            btnContinental.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("localForeign", "continental");
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                    autoRec.setArguments(bundle);
                    ft.replace(R.id.frag_main, autoRec).addToBackStack(null)
                            .commit();
                }
            });
            btnIndian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("localForeign", "indian");
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                    autoRec.setArguments(bundle);
                    ft.replace(R.id.frag_main, autoRec).addToBackStack(null)
                            .commit();
                }
            });
            btnfastFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("localForeign", "fast");
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                    autoRec.setArguments(bundle);
                    ft.replace(R.id.frag_main, autoRec).addToBackStack(null)
                            .commit();
                }
            });
        }
        return rootView;
    }

    public void loadDietDialog()
    {

        tvMessage.setText("Search for Restaurants based on Dietary Restrictions?");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineMain.setVisibility(View.GONE);
                lineDiet.setVisibility(View.VISIBLE);
                tvMessage.setText("Which is your preference?");
                }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLocalForeign();
            }
        });
    }

    public void loadLocalForeign()
    {

        btnYes.setText("Foreign");
        btnNo.setText("Local");

        tvMessage.setText("Do you want to eat at a Foreign or local Restaurant?");

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineMain.setVisibility(View.GONE);
                linefore.setVisibility(View.VISIBLE);
                tvMessage.setText("Which is your preference?");
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineMain.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                tvMessage.setText("Which is your preference?");
            }
        });
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
    public void onResume() {
        super.onResume();

        getActivity().setTitle("Restaurant Advisor");
        if (lineMain.getVisibility() == View.GONE) {
            linearLayout.setVisibility(View.GONE);
            lineMain.setVisibility(View.VISIBLE);
            lineDiet.setVisibility(View.GONE);
            linefore.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
