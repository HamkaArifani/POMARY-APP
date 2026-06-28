package com.example.pomaryapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pomaryapp.R
import com.example.pomaryapp.core.components.PomaryCard
import com.example.pomaryapp.core.components.PomaryHeader
import com.example.pomaryapp.core.utils.toRupiah
import com.example.pomaryapp.domain.model.PreorderModel
import com.example.pomaryapp.domain.repository.AuthRepository
import com.example.pomaryapp.ui.auth.LoginViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            PomaryHeader(onClick = {navController.navigate("settings")})
        }
    ) { innerPadding ->
        if (state.isLoading){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else{
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                SectionTitle(
                    title = stringResource(R.string.active_preorder),
                    onAddClick = { navController.navigate("preorder_form") },
                )

                LazyRowContent(state.activePreorders, R.color.preorder_card) { poId ->
                    navController.navigate("preorder_detail/$poId")
                }

                Spacer(modifier = Modifier.height(50.dp))

                SectionTitle(
                    title = stringResource(R.string.historyText)
                )

                LazyRowContent(state.completedPreorders, R.color.history_preorder) { poId ->
                    navController.navigate("preorder_detail/$poId")
                }
            }
        }
    }
}

@Composable
fun SectionTitle(
    title: String,
    onAddClick: (()-> Unit)? = null,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (onAddClick != null) {
                IconButton(onClick = onAddClick) {
                    Icon(
                        Icons.Default.AddCircle,
                        contentDescription = null,
                        tint = Color.Black,

                    )
                }
            }
        }
    }
}

@Composable
fun HomePreorderCard(
    preorder: PreorderModel,
    cardColor: Color,
    onClick: () -> Unit
) {
    PomaryCard(
        modifier = Modifier
            .width(260.dp)
            .clickable { onClick() },
        containerColor = cardColor
    ) {
        Text(text = preorder.title, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(text = preorder.productName, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = stringResource(R.string.total_order), color = Color.White, style = MaterialTheme.typography.labelSmall)
                Text(text = "${preorder.totalOrders} Qty", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = stringResource(R.string.clean_profit), color = Color.White, style = MaterialTheme.typography.labelSmall)
                Text(text = preorder.totalCleanProfit.toRupiah(), color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun LazyRowContent(
    list: List<PreorderModel>,
    colorId: Int,
    onItemClick: (String) -> Unit
) {
    if (list.isEmpty()) {
        Text(stringResource(R.string.zero_card), Modifier.padding(16.dp), color = Color.Gray)
    } else {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(list) { po ->
                HomePreorderCard(
                    preorder = po,
                    cardColor = colorResource(id = colorId),
                    onClick = { onItemClick(po.preorderId) }
                )
            }
        }
    }
}