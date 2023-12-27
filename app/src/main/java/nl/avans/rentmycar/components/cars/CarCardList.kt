//package nl.avans.rentmycar.components.cars
//
//import android.widget.ScrollView
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import nl.avans.rentmycar.R
//import nl.avans.rentmycar.model.Car
//
//@Composable
//fun CarCardList(cars: List<Car>) {
//    val painter = painterResource(id = R.drawable.monk)
//    val title = "A new car!"
//
//    LazyColumn {
//        items(1) {index ->
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceAround
//            ){
//                CarCard(painter = painter, title = title, modifier = Modifier.weight(1.0F))
//            }
//        }
//    }
//}