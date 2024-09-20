package ai.mlc.mlcchat

import ai.mlc.mlcchat.ui.theme.MLCChatTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {

    init {
        System.loadLibrary("oxigraph")
    }

    private external fun loadData(input: String): String?

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val output: String = loadData("Luke").toString()
        println(output)

        setContent {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                MLCChatTheme {
//                    NavView()

                    val appViewModel: AppViewModel = viewModel()
                    val chatState: AppViewModel.ChatState = appViewModel.chatState
                    appViewModel.modelList[0].startChat()

                    val questionList = arrayOf(
                        "Are you free to attend our family dinner on Saturday evening?",
                        "Can you pick up the kids from school on Wednesday afternoon?",
                        "Do you have time to help with the garden this weekend?",
                        "When can we plan our next family outing?",
                        "Are you available to visit Grandma next Thursday?",
                        "Can we meet for lunch next Tuesday?",
                        "Are you free for a movie night this Friday?",
                        "Do you have time to catch up over coffee this weekend?",
                        "Are you available to join our group hike on Saturday?",
                        "When can we plan our next game night?",
                        "Are you available for a team meeting on Thursday afternoon?",
                        "Can we schedule a quick check-in before the project deadline?",
                        "Do you have time to review the report tomorrow morning?",
                        "When can we plan our next brainstorming session?",
                        "Are you free for a lunch meeting with the new client on Tuesday?",
                        "Can you attend the quarterly review meeting on Monday?",
                        "Are you available for a one-on-one meeting this week?",
                        "When can we discuss your progress on the current project?",
                        "Can you join the leadership meeting on Thursday afternoon?",
                        "Are you free to prepare for the client presentation on Wednesday?"
                    )

//                    var prompt = "Alex has the following schedules for this month, and today is August 1st, 2024.\n" +
//                            "Alex's schedules are described in the form of a JSON file.\n"

//                    val jsonContent = """
//                        {
//                          "AlexCalendar2024": {
//                            "August": {
//                              "MeetingWithFriends": [
//                                {"event": "Catch-up with Friends", "date": "2024-08-03", "time": "16:00 - 17:30"},
//                                {"event": "Hiking Trip", "date": "2024-08-10", "time": "06:00 - 12:00"},
//                                {"event": "Friends' Game Night", "date": "2024-08-17", "time": "20:00 - 23:00"},
//                                {"event": "Dinner with Friends", "date": "2024-08-24", "time": "18:00 - 20:00"},
//                                {"event": "Beach Day with Friends", "date": "2024-08-31", "time": "09:00 - 14:00"}
//                              ],
//                              "OfficeMeetings": [
//                                {"event": "Team Meeting", "date": "2024-08-01", "time": "09:00 - 10:00"},
//                                {"event": "Design Review", "date": "2024-08-02", "time": "10:00 - 11:00"},
//                                {"event": "Weekly Sync-Up", "date": "2024-08-05", "time": "09:00 - 10:00"},
//                                {"event": "One-on-One Meeting", "date": "2024-08-06", "time": "10:00 - 11:00"},
//                                {"event": "Stand-up Meeting", "date": "2024-08-07", "time": "09:00 - 09:30"},
//                                {"event": "Client Feedback Session", "date": "2024-08-08", "time": "10:00 - 11:00"},
//                                {"event": "Retrospective Meeting", "date": "2024-08-09", "time": "09:00 - 10:00"},
//                                {"event": "Project Kickoff", "date": "2024-08-12", "time": "10:00 - 11:00"},
//                                {"event": "Client Call", "date": "2024-08-13", "time": "09:00 - 10:00"},
//                                {"event": "Design Review", "date": "2024-08-14", "time": "10:00 - 11:00"},
//                                {"event": "Product Launch Meeting", "date": "2024-08-15", "time": "10:00 - 11:00"},
//                                {"event": "Weekly Sync-Up", "date": "2024-08-19", "time": "09:00 - 10:00"},
//                                {"event": "One-on-One Meeting", "date": "2024-08-20", "time": "10:00 - 11:00"},
//                                {"event": "Stand-up Meeting", "date": "2024-08-21", "time": "09:00 - 09:30"},
//                                {"event": "Client Feedback Session", "date": "2024-08-22", "time": "10:00 - 11:00"},
//                                {"event": "Retrospective Meeting", "date": "2024-08-23", "time": "09:00 - 10:00"},
//                                {"event": "Project Kickoff", "date": "2024-08-26", "time": "10:00 - 11:00"},
//                                {"event": "Client Call", "date": "2024-08-27", "time": "09:00 - 10:00"},
//                                {"event": "Design Review", "date": "2024-08-28", "time": "10:00 - 11:00"},
//                                {"event": "Product Launch Meeting", "date": "2024-08-29", "time": "10:00 - 11:00"},
//                                {"event": "Team Building Activity", "date": "2024-08-30", "time": "11:00 - 13:00"}
//                              ],
//                              "DailyRoutineEvents": [],
//                              "Classes": [],
//                              "FamilyEvents": [
//                                {"event": "Family Dinner", "date": "2024-08-03", "time": "18:00 - 20:00"},
//                                {"event": "Family Outing", "date": "2024-08-10", "time": "10:00 - 16:00"},
//                                {"event": "Visit to Grandparents", "date": "2024-08-17", "time": "14:00 - 18:00"},
//                                {"event": "Family Movie Night", "date": "2024-08-24", "time": "19:00 - 21:00"},
//                                {"event": "Family Picnic", "date": "2024-08-31", "time": "11:00 - 15:00"}
//                              ],
//                              "FestivalHolidays": []
//                            },
//                        }
//                        """

//                    val raw_jsonContent = """
//                        {
//                          "AlexCalendar2024": {
//                            "August": {
//                              "MeetingWithFriends": [
//                                {"event": "Catch-up with Friends", "date": "2024-08-03", "time": "16:00 - 17:30"},
//                                {"event": "Hiking Trip", "date": "2024-08-10", "time": "06:00 - 12:00"},
//                                {"event": "Friends' Game Night", "date": "2024-08-17", "time": "20:00 - 23:00"},
//                                {"event": "Dinner with Friends", "date": "2024-08-24", "time": "18:00 - 20:00"},
//                                {"event": "Beach Day with Friends", "date": "2024-08-31", "time": "09:00 - 14:00"}
//                              ],
//                        }
//                        """
//                    prompt += raw_jsonContent

                    var prompt = "Today is August 1st, 2024.\n" +
                            "Alex's schedules are described in the form of RDF triples as follows.\n"
                    val knowledgeTriples = """
                        {
                            wd:Alex wd:has wd:FamilyDinner .
                            wd:FamilyDinner wd:isOn "2024-08-03 18:00 - 20:00" .
                        }
                        """
                    prompt += knowledgeTriples

                    val questionTemplate = "\nCould you generate three reasonable responses for Alex to the question below?\n" +
                            "Question: "
                    prompt += questionTemplate

                    val totalQuestionNumber: Int = questionList.size
//                    for (i in 0 until totalQuestionNumber) {
                        for (i in 0 until 1) {
                        chatState.generateAnswer(prompt + questionList[i], i, totalQuestionNumber)
                    }
                }
            }
        }
    }
}
