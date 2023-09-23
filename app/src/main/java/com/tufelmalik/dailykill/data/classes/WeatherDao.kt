import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tufelmalik.dailykill.data.model.weather.WeatherModel

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWeather(weatherModel: WeatherModel)

    @Query("SELECT * FROM weather")
    fun getSavedWeather(): LiveData<List<WeatherModel>>
}

