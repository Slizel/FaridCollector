package faridnet.com.faridcollector.Data.ContagensData

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import faridnet.com.faridcollector.Data.ProdutosData.Produtos
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Entity(
    tableName = "contagem_table"
//    ,foreignKeys = [ForeignKey(
//        entity = Produtos::class,
//        parentColumns =["produtoId"],
//        childColumns = ["produtoId"] )
//    ]
)

data class Contagens(

    @PrimaryKey(autoGenerate = true)
    val produtoId: Int,
    val quantidade: String,
    val dataHora: String
)
