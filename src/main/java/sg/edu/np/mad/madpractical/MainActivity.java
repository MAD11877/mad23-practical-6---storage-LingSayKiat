package sg.edu.np.mad.madpractical;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter adapter;

    private Button followButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DbHandler dbHandler = new DbHandler(this);
        List<User> userList = dbHandler.getUsers();

        adapter = new UserAdapter(userList, this); // Pass the context (this) as the second argument
        recyclerView.setAdapter(adapter);

        followButton = findViewById(R.id.followButton); // Find the followButton by its ID
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the followed status for the user
                User user = adapter.getSelectedUser(); // Get the selected user from the adapter

                if (user != null) {
                    user.setFollowed(!user.isFollowed());

                    // Update the user in the database
                    dbHandler.updateUser(user);

                    // Update the UI or perform any other necessary actions
                    adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
                }
            }
        });
    }
}
