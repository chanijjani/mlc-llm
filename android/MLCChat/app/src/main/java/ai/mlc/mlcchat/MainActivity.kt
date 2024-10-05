package ai.mlc.mlcchat

import ai.mlc.mlcchat.ui.theme.MLCChatTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
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

                    val questionType = 1;   // 0: Schedule, 1: Entertainment
                    val isRawData = false
                    var questionList: Array<String> = arrayOf("", "")
                    var prompt = ""
                    val rawData: String
                    val knowledgeTriples: String
                    val questionTemplate = "\nCould you generate three reasonable responses for Alex to the question below?\n" +
                            "Question: "
                    when (questionType) {
                        0 -> {
                            questionList = arrayOf(
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

                            if (isRawData) {
                                prompt = "Alex has the following schedules for this month, and today is August 1st, 2024.\n" +
                                        "Alex's schedules are described in the form of a JSON file.\n"

                                //                    val rawData = """
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

                                rawData = """
                                    {
                                      "AlexCalendar2024": {
                                        "August": {
                                          "MeetingWithFriends": [
                                            {"event": "Catch-up with Friends", "date": "2024-08-03", "time": "16:00 - 17:30"},
                                            {"event": "Hiking Trip", "date": "2024-08-10", "time": "06:00 - 12:00"},
                                            {"event": "Friends' Game Night", "date": "2024-08-17", "time": "20:00 - 23:00"},
                                            {"event": "Dinner with Friends", "date": "2024-08-24", "time": "18:00 - 20:00"},
                                            {"event": "Beach Day with Friends", "date": "2024-08-31", "time": "09:00 - 14:00"}
                                          ],
                                    }
                                    """
                                prompt += rawData
                            } else {
                                prompt = "Today is August 1st, 2024.\n" +
                                        "Alex's schedules are described in the form of RDF triples as follows.\n"
                                knowledgeTriples = """
                                    {
                                        wd:Alex wd:has wd:FamilyDinner .
                                        wd:FamilyDinner wd:isOn "2024-08-03 18:00 - 20:00" .
                                    }
                                    """
                                prompt += knowledgeTriples
                            }
                            prompt += questionTemplate
                        }
                        1 -> {
                            questionList = arrayOf(
                                "What did you think of the Oscars last night?",
                                "How did 'Everything Everywhere All at Once' perform at the Oscars?",
                                "What’s your take on Michelle Yeoh winning Best Actress?",
                                "Did Michelle Yeoh make history with her Oscar win?",
                                "How did Ke Huy Quan's Oscar win make you feel?",
                                "Why was Ke Huy Quan's journey to an Oscar so emotional?",
                                "What did you think of the Indiana Jones reunion on the Oscars stage?",
                                "How do you feel about Brendan Fraser's comeback and Oscar win?",
                                "Why was Brendan Fraser's win for 'The Whale' so special?",
                                "What were your thoughts on Lady Gaga’s performance at the Oscars?",
                                "How did Rihanna's performance compare to Lady Gaga's at the Oscars?",
                                "What message did Rihanna convey during her performance at the Oscars?",
                                "Do you think next year's Oscars will be bigger than this year's?",
                                "Should watching the Oscars become a tradition for us?",
                                "Which Oscar moment gave you chills last night?",
                                "What did you think of the Oscar performances overall?",
                                "How did Michelle Yeoh’s Oscar speech impact you?",
                                "Why do you think 'Everything Everywhere All at Once' swept so many categories?",
                                "What was the highlight of this year’s Oscars for you?",
                                "How did the Oscar performances this year compare to previous years?"
                            )

                            if (isRawData) {
                                prompt = "Alex's chatting with friends are described as follows.\n"
                                rawData =
                                    "Alex: Did you guys watch the Oscars last night? That was one of the best shows in years!\n" +
                                            "\n" +
                                            "Ryan: I did! I can’t believe Everything Everywhere All at Once swept so many categories. It deserved every win, though. What a wild movie!\n" +
                                            "\n" +
                                            "Sarah: Totally! Michelle Yeoh winning Best Actress was everything. She made history, and her speech gave me chills. She’s such a legend.\n" +
                                            "\n" +
                                            "Jason: Yeah, her speech was powerful. And Ke Huy Quan winning Best Supporting Actor? I’m still emotional over that. His journey from child actor to Oscar winner is so inspiring.\n" +
                                            "\n" +
                                            "Emily: I loved how he thanked his mom! And when he reunited with Harrison Ford on stage, my heart melted. It was like an Indiana Jones moment all over again!\n" +
                                            "\n" +
                                            "Alex: Right?! And I’m so happy Brendan Fraser won Best Actor for The Whale. His comeback story is incredible. I was rooting for him all the way.\n" +
                                            "\n" +
                                            "Ryan: Fraser’s win was one of the highlights for me too. It was his moment, and you could tell how much it meant to him. The guy’s been through a lot, and now he’s an Oscar winner!\n" +
                                            "\n" +
                                            "Sarah: And can we talk about the performances? Lady Gaga’s “Hold My Hand” was beautiful. She stripped it down and just let her voice shine.\n" +
                                            "\n" +
                                            "Jason: Gaga was great, but I also loved Rihanna’s performance. “Lift Me Up” hit all the emotional notes. Her voice is so powerful, and seeing her pregnant on stage was such a moment!\n" +
                                            "\n" +
                                            "Alex: The Oscars really delivered this year. We should make it a tradition to watch together! Next year’s awards night could be even bigger."
                                prompt += rawData
                            } else {
                                prompt = "Alex's chatting with friends are described in the form of RDF triples as follows.\n"
                                knowledgeTriples = """
                                    {
                                        wd:You wd:commented "Did you guys watch the Oscars last night? That was one of the best shows in years!" .
                                        wd:Ryan wd:commented "I did! I can’t believe Everything Everywhere All at Once swept so many categories. It deserved every win, though. What a wild movie!" .
                                        wd:Sarah wd:commented "Totally! Michelle Yeoh winning Best Actress was everything. She made history, and her speech gave me chills. She’s such a legend." .
                                        wd:Jason wd:commented "Yeah, her speech was powerful. And Ke Huy Quan winning Best Supporting Actor? I’m still emotional over that. His journey from child actor to Oscar winner is so inspiring." .
                                        wd:Emily wd:commented "I loved how he thanked his mom! And when he reunited with Harrison Ford on stage, my heart melted. It was like an Indiana Jones moment all over again!" .
                                        wd:You wd:commented "Right?! And I’m so happy Brendan Fraser won Best Actor for The Whale. His comeback story is incredible. I was rooting for him all the way." .
                                        wd:Ryan wd:commented "Fraser’s win was one of the highlights for me too. It was his moment, and you could tell how much it meant to him. The guy’s been through a lot, and now he’s an Oscar winner!" .
                                        wd:Sarah wd:commented "And can we talk about the performances? Lady Gaga’s “Hold My Hand” was beautiful. She stripped it down and just let her voice shine." .
                                        wd:Jason wd:commented "Gaga was great, but I also loved Rihanna’s performance. “Lift Me Up” hit all the emotional notes. Her voice is so powerful, and seeing her pregnant on stage was such a moment!" .
                                        wd:You wd:commented "The Oscars really delivered this year. We should make it a tradition to watch together! Next year’s awards night could be even bigger." .
                                    }
                                    """
                                prompt += knowledgeTriples
                            }
                            prompt += questionTemplate
                        }
                    }
//                    val totalQuestionNumber: Int = questionList.size
                    val totalQuestionNumber: Int = 5
                    for (i in 0 until totalQuestionNumber) {
                        chatState.generateAnswer(prompt + questionList[i], i, totalQuestionNumber)
                    }
                }
            }
        }
    }
}
