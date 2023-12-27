package nl.avans.rentmycar.ui.screens


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.avans.rentmycar.model.Car
import nl.avans.rentmycar.ui.car.CarViewModel

// viewModel: MainViewModel = viewModel()
@Composable
fun CarsScreen() {
    val carViewModel = viewModel(modelClass = CarViewModel::class.java)
    val state by carViewModel.state.collectAsState()
    LazyColumn {
        if(state.isEmpty()) {
            items(1) {
                Text(text = "test")
            }
        } else {
            items(state) { car: Car ->
                Text(text = "test")
                //CarCard(painter = painterResource(id = R.drawable.monk), title = car.make, modifier = Modifier)
            }
        }
    }




//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        CarCardList()
//        Text(text = "Cars screen")
//    }
}