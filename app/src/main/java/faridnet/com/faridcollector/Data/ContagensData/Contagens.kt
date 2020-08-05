package faridnet.com.faridcollector.Data.ContagensData

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import faridnet.com.faridcollector.Data.ProdutosData.Produtos

@Entity(
    tableName = "contagem_table"
//    ,foreignKeys = [ForeignKey(
//        entity = Produtos::class,
//        parentColumns =["produtoId"],
//        childColumns = ["produtoId"] )
//    ]
)

data class Contagens (

    @PrimaryKey(autoGenerate = true)
    val contagemId: Int,
    val produtoId: Int,
    val quantidade: String
    //val dataHora: Date
)