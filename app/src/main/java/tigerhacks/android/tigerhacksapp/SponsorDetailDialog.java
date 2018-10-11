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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

@SuppressLint("ValidFragment")
public class SponsorDetailDialog extends DialogFragment {

    private Sponsor sponsorData;

    private ImageView mainImage;
    private TextView nameText;
    private TextView secondText;
    private TextView linkText;
    private TextView descriptionText;
    private ImageView backButton;
    private LinearLayout mentorLayout;

    @SuppressLint("ValidFragment")
    public SponsorDetailDialog(Sponsor sponsor) {
        sponsorData = sponsor;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sponsors_detail, container, false);

        mainImage = view.findViewById(R.id.mainImage);
        nameText = view.findViewById(R.id.sponsorName);
        secondText = view.findViewById(R.id.secondText);
        linkText = view.findViewById(R.id.linkText);
        descriptionText = view.findViewById(R.id.descriptionText);
        backButton = view.findViewById(R.id.backButton);
        mentorLayout = view.findViewById(R.id.mentorLayout);

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
}
