package com.limbergdv.vivia_mobile.features.follows.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.limbergdv.vivia_mobile.features.follows.presentation.viewmodels.FollowsEvent
import com.limbergdv.vivia_mobile.features.follows.presentation.viewmodels.FollowsViewModel
import com.limbergdv.vivia_mobile.features.users.lessors.domain.entities.Lessor

@Composable
fun FollowsScreen(
    viewModel: FollowsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(FollowsEvent.ConsumeError)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 48.dp, start = 24.dp, end = 24.dp)
    ) {
        Text(
            text = "Descubre Arrendadores",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Sigue a tus favoritos para no perderte sus propiedades.",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (state.isLoading && state.lessors.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Black)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.lessors) { lessor ->
                    LessorItem(
                        lessor = lessor,
                        isFollowed = state.followedCompanies.contains(lessor.companyName),
                        onFollowClick = {
                            viewModel.onEvent(FollowsEvent.OnFollowClicked(lessor.companyName))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LessorItem(
    lessor: Lessor,
    isFollowed: Boolean,
    onFollowClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.medium)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = lessor.companyName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = "${lessor.firstName} ${lessor.lastName}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }

        Button(
            onClick = onFollowClick,
            enabled = !isFollowed,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFollowed) Color.LightGray else Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFE0E0E0),
                disabledContentColor = Color.Gray
            )
        ) {
            Text(text = if (isFollowed) "Siguiendo" else "Seguir")
        }
    }
}