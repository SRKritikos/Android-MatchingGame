package sl.on.ca.comp208.imagematcher.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import sl.on.ca.comp208.imagematcher.R;
import sl.on.ca.comp208.imagematcher.model.UserScore;

/**
 * Created by Steven on 1/25/2017.
 */

public class TopScoresAdapter extends ArrayAdapter<UserScore> {
    Context context;
    List<UserScore> topUserScores;

    public TopScoresAdapter(Context context, List<UserScore> topUserScores) {
        super(context, R.layout.topscore_list_layout, topUserScores);
        this.context = context;
        this.topUserScores = topUserScores;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(R.layout.topscore_list_layout, parent, false);
        }

        UserScore userScore = this.topUserScores.get(position);

        TextView topScoreText = (TextView) convertView.findViewById(R.id.topScoreText);
        TextView topTimeText = (TextView) convertView.findViewById(R.id.topTimeText);

        topScoreText.setText( String.valueOf(userScore.getScore()) );
        topTimeText.setText( userScore.getMinutes()+":"+userScore.getSeconds());

        return convertView;
    }
}
