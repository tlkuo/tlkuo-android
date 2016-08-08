package tw.frb.getstart;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment {

    public final static String ARG_POSITION = "tw.frb.getstart.POSITION";

    public ArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();

        if (args != null) {
            updateArticleView(args.getInt(ARG_POSITION));
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    public void updateArticleView(int position) {
        Log.d("args", String.valueOf(position));

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int prefPosition = sharedPref.getInt(getString(R.string.saved_position), 0);

        Log.d("pref", String.valueOf(prefPosition));
    }
}
