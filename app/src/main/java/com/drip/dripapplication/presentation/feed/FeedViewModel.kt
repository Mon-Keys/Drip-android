package com.drip.dripapplication.presentation.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.drip.dripapplication.domain.model.User

class FeedViewModel : ViewModel() {
    private val _userInfo = MutableLiveData<User?>()
    val userInfo: LiveData<User?> = _userInfo

    private val users = listOf(
        User(
        "Алиса",
        24,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
        listOf(
            "https://www.gstatic.com/webp/gallery/1.webp",
            "https://t3.ftcdn.net/jpg/04/64/39/50/240_F_464395072_dodQ5vK3ynIPDbD9nG82clMWJL4zUfMa.jpg",
            "https://t4.ftcdn.net/jpg/04/72/09/53/240_F_472095328_nNAedzTl57kxTyzs0wGrkvu3R2MQlvSq.jpg",
            "https://t3.ftcdn.net/jpg/04/64/85/42/240_F_464854295_GCNWjI5hPGj8hkWk7Ix8lO0xCSeo2jMc.jpg",
            "https://t3.ftcdn.net/jpg/04/64/62/34/240_F_464623498_GteYUp2YtOWCBBoIdUuEBe44Cu4HHZSN.jpg",
            "https://t3.ftcdn.net/jpg/04/64/52/06/240_F_464520694_HsFdx2BWUrKRFOdxyM1ATk5wvbqeHttR.jpg",
            "https://t4.ftcdn.net/jpg/04/65/40/27/240_F_465402775_ltrRik2AqHgmHz4JgIAHTFFJqWnlRN5F.jpg",
            "https://t4.ftcdn.net/jpg/04/69/33/37/240_F_469333729_ohdQnuuAehaGlhWQD1zh4i3MNQb9QMFz.jpg"
        ),
        listOf("Учеба","Работа", "Отдых","Друзья","Кино","Путешествия","Книги")
    ),
        User(
            "Вера",
            20,
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
            listOf(
                "https://t3.ftcdn.net/jpg/04/64/39/50/240_F_464395072_dodQ5vK3ynIPDbD9nG82clMWJL4zUfMa.jpg",
                "https://t4.ftcdn.net/jpg/04/72/09/53/240_F_472095328_nNAedzTl57kxTyzs0wGrkvu3R2MQlvSq.jpg",
                "https://t3.ftcdn.net/jpg/04/64/85/42/240_F_464854295_GCNWjI5hPGj8hkWk7Ix8lO0xCSeo2jMc.jpg",
                "https://t3.ftcdn.net/jpg/04/64/62/34/240_F_464623498_GteYUp2YtOWCBBoIdUuEBe44Cu4HHZSN.jpg",
                "https://t3.ftcdn.net/jpg/04/64/52/06/240_F_464520694_HsFdx2BWUrKRFOdxyM1ATk5wvbqeHttR.jpg",
                "https://t4.ftcdn.net/jpg/04/65/40/27/240_F_465402775_ltrRik2AqHgmHz4JgIAHTFFJqWnlRN5F.jpg",
                "https://t4.ftcdn.net/jpg/04/69/33/37/240_F_469333729_ohdQnuuAehaGlhWQD1zh4i3MNQb9QMFz.jpg"
            ),
            listOf("Учеба","Работа", "Отдых","Друзья","Кино","Путешествия","Книги")
        ),
        User(
            "Саша",
            19,
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
            listOf(
                "https://t3.ftcdn.net/jpg/04/64/39/50/240_F_464395072_dodQ5vK3ynIPDbD9nG82clMWJL4zUfMa.jpg",
                "https://t4.ftcdn.net/jpg/04/72/09/53/240_F_472095328_nNAedzTl57kxTyzs0wGrkvu3R2MQlvSq.jpg",
                "https://t3.ftcdn.net/jpg/04/64/85/42/240_F_464854295_GCNWjI5hPGj8hkWk7Ix8lO0xCSeo2jMc.jpg",
                "https://t3.ftcdn.net/jpg/04/64/62/34/240_F_464623498_GteYUp2YtOWCBBoIdUuEBe44Cu4HHZSN.jpg",
                "https://t3.ftcdn.net/jpg/04/64/52/06/240_F_464520694_HsFdx2BWUrKRFOdxyM1ATk5wvbqeHttR.jpg",
                "https://t4.ftcdn.net/jpg/04/65/40/27/240_F_465402775_ltrRik2AqHgmHz4JgIAHTFFJqWnlRN5F.jpg",
                "https://t4.ftcdn.net/jpg/04/69/33/37/240_F_469333729_ohdQnuuAehaGlhWQD1zh4i3MNQb9QMFz.jpg"
            ),
            listOf("Учеба","Работа", "Отдых","Друзья","Кино","Путешествия","Книги")
        ),
        User(
            "Катя",
            32,
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
            listOf(
                "https://t3.ftcdn.net/jpg/04/64/39/50/240_F_464395072_dodQ5vK3ynIPDbD9nG82clMWJL4zUfMa.jpg",
                "https://t4.ftcdn.net/jpg/04/72/09/53/240_F_472095328_nNAedzTl57kxTyzs0wGrkvu3R2MQlvSq.jpg",
                "https://t3.ftcdn.net/jpg/04/64/85/42/240_F_464854295_GCNWjI5hPGj8hkWk7Ix8lO0xCSeo2jMc.jpg",
                "https://t3.ftcdn.net/jpg/04/64/62/34/240_F_464623498_GteYUp2YtOWCBBoIdUuEBe44Cu4HHZSN.jpg",
                "https://t3.ftcdn.net/jpg/04/64/52/06/240_F_464520694_HsFdx2BWUrKRFOdxyM1ATk5wvbqeHttR.jpg",
                "https://t4.ftcdn.net/jpg/04/65/40/27/240_F_465402775_ltrRik2AqHgmHz4JgIAHTFFJqWnlRN5F.jpg",
                "https://t4.ftcdn.net/jpg/04/69/33/37/240_F_469333729_ohdQnuuAehaGlhWQD1zh4i3MNQb9QMFz.jpg"
            ),
            listOf("Учеба","Работа", "Отдых","Друзья","Кино","Путешествия","Книги")
        )
    )
    private var currentIndex = 0

    init {
        getUserInfo()
    }

    fun getUserInfo(){
        _userInfo.value = users[currentIndex]
    }

    fun swipe(){
        println(currentIndex)
        currentIndex += 1
    }
}