package info.lostandfound.firebase.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.lostandfound.firebase.Adapter.NavigationDrawerAdapter;
import info.lostandfound.firebase.Adapter.RecyclerAdapter;
import info.lostandfound.firebase.Model.NavDrawerItem;
import info.lostandfound.firebase.Model.Upload;
import info.lostandfound.firebase.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class FragmentDrawer extends Fragment{

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout,drawer;
    private List<Upload> uploadsFiltered;
    private TextView postusername;
    private Button Books,Accessories,Clothing,Persons,electronics,other,Home;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private RecyclerAdapter mAdapter;
    private Context context;
    private FragmentDrawerListener drawerListener;
    private List<Upload> uploads;
    public FragmentDrawer() {


    }



    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        postusername=layout.findViewById(R.id.postusername);
        Books=layout.findViewById(R.id.Books_documents);
        Accessories=layout.findViewById(R.id.accessories);
        Clothing=layout.findViewById(R.id.clothing);
        Persons=layout.findViewById(R.id.Persons);
        electronics=layout.findViewById(R.id.electronics);
        other=layout.findViewById(R.id.Other);
        Home=layout.findViewById(R.id.Home);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user !=null) {
            postusername.setText(user.getDisplayName());
        }
        uploads = new ArrayList<>();





        Books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent type = new Intent(getContext(), MainActivity.class);
                type.putExtra("filterBooks_documents","Books_documents");
                startActivity(type);


            }
            });
        Accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent type = new Intent(getContext(), MainActivity.class);
                type.putExtra("filterBooks_documents","Accessories");
                startActivity(type);


            }
        });
        Clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent type = new Intent(getContext(), MainActivity.class);
                type.putExtra("filterBooks_documents","Clothing");
                startActivity(type);


            }
        });
        Persons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent type = new Intent(getContext(), MainActivity.class);
                type.putExtra("filterBooks_documents","Persons");
                startActivity(type);


            }
        });
        electronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent type = new Intent(getContext(), MainActivity.class);
                type.putExtra("filterBooks_documents","Electronics");
                startActivity(type);


            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent type = new Intent(getContext(), MainActivity.class);
                type.putExtra("filterBooks_documents","Other");
                startActivity(type);


            }
        });

      /*  adapter = new NavigationDrawerAdapter(getActivity(), getData());
        mAdapter = new RecyclerAdapter(getActivity(), uploads);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/

        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }



        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }




}