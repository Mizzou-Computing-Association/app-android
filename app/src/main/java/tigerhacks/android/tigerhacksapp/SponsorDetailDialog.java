package tigerhacks.android.tigerhacksapp;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

@SuppressLint("ValidFragment")
public class SponsorDetailDialog extends DialogFragment {

    private Sponsor sponsorData;

    private TextView linkText;

    @SuppressLint("ValidFragment")
    public SponsorDetailDialog(Sponsor sponsor) {
        sponsorData = sponsor;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sponsors_detail, container, false);

        ImageView mainImage = view.findViewById(R.id.mainImage);
        TextView nameText = view.findViewById(R.id.sponsorName);
        TextView secondText = view.findViewById(R.id.secondText);
        linkText = view.findViewById(R.id.linkText);
        TextView descriptionText = view.findViewById(R.id.descriptionText);
        ImageView backButton = view.findViewById(R.id.backButton);
        LinearLayout mentorLayout = view.findViewById(R.id.mentorLayout);

        nameText.setText(sponsorData.getName());
        secondText.setText(sponsorData.getLocation());
        linkText.setText(sponsorData.getWebsite());
        descriptionText.setText(sponsorData.getDescription());

        Picasso.get().load(sponsorData.getImage()).into(mainImage);

        linkText.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkText.getText().toString()));
                startActivity(browserIntent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { dismiss(); } });

        for (Mentor mentor : sponsorData.getMentors()) {
            LinearLayout mentorContainer = new LinearLayout(getContext());
            mentorContainer.setOrientation(LinearLayout.VERTICAL);
            mentorContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView name = new TextView(getContext());
            name.setText(mentor.getName());
            name.setTypeface(null, Typeface.BOLD);
            TextView contact = new TextView(getContext());
            contact.setText(mentor.getContact());
            TextView skills = new TextView(getContext());
            skills.setText(mentor.getSkills());

            mentorContainer.addView(name);
            mentorContainer.addView(contact);
            mentorContainer.addView(skills);
            mentorContainer.addView(new TextView(getContext())); // cheap space

            mentorLayout.addView(mentorContainer);

            Log.e("MARK", "1");

        }

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
