import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tufelmalik.dailykill.data.classes.NewsDao
import com.tufelmalik.dailykill.data.model.NewsModel

@Database(entities = [NewsModel::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        private var instance: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): NewsDatabase {
            return Room.databaseBuilder(context, NewsDatabase::class.java, "news_db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
