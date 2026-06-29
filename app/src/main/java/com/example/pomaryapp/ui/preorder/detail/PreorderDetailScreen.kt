package com.example.pomaryapp.ui.preorder.detail

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pomaryapp.R
import com.example.pomaryapp.core.components.PomaryButton
import com.example.pomaryapp.core.components.PomaryCard
import com.example.pomaryapp.core.components.PomaryHeader
import com.example.pomaryapp.core.utils.Constants
import com.example.pomaryapp.core.utils.PdfGenerator
import com.example.pomaryapp.core.utils.WhatsAppHelper
import com.example.pomaryapp.core.utils.toRupiah
import com.example.pomaryapp.domain.model.OrderModel

@Composable
fun PreorderDetailScreen(
    preorderId: String,
    navController: NavController,
    viewModel: PreorderDetailViewModel = hiltViewModel()
) {
    val po by viewModel.preorder.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val userSession by viewModel.userSession.collectAsState()
    val context = LocalContext.current

    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        uri?.let {
            po?.let { currentPo ->
                PdfGenerator.export(context, it, currentPo, orders)
            }
        }
    }

    LaunchedEffect(Unit) { viewModel.load(preorderId) }

    Scaffold(
        topBar = {
            PomaryHeader(
                icon = Icons.Default.Home,
                iconLabel = stringResource(R.string.home),
                onClick = {
                    navController.navigate("home"){
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    ) { padding ->
        po?.let { data ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    PomaryCard(
                        containerColor = colorResource(
                            id = if (data.isCompleted) R.color.history_preorder else R.color.preorder_card
                        ),
                        modifier = Modifier
                            .clickable {
                                navController.navigate("preorder_form?preorderId=${data.preorderId}")
                            }
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = data.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${stringResource(R.string.product_name)} ${data.productName}",
                            color = Color.White,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${stringResource(R.string.total_order)} ${data.totalOrders}",
                            color = Color.White,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                item{
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        DetailActionItem(
                            Icons.Default.Calculate,
                            stringResource(R.string.action_recap)
                        ) {
                            viewModel.showRecapDialog.value = true
                        }
                        DetailActionItem(
                            Icons.Default.AddShoppingCart,
                            stringResource(R.string.add_order_btn)
                        ) {
                            navController.navigate("order_form?preorderId=${data.preorderId}")
                        }
                        DetailActionItem(
                            Icons.Default.PictureAsPdf,
                            stringResource(R.string.export_resume_btn)
                        ) {
                            val fileName = "${po?.title?.replace(" ", "_") ?: "Preorder"}.pdf"
                            pdfLauncher.launch(fileName)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    if (!data.isCompleted) {
                        PomaryButton(
                            text = stringResource(R.string.finish_po),
                            onClick = { viewModel.finishPo() },
                            containerColor = Color(0xFF4CAF50)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                item {
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.order_list_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )
                }

                if (orders.isEmpty()) {
                    item {
                        Text(
                            stringResource(R.string.no_orders),
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                } else {
                    items(orders) { order ->
                        OrderListItem(
                            order = order,
                            isPreorderCompleted = data.isCompleted,
                            onCardClick = {
                                navController.navigate("order_form?preorderId=${data.preorderId}&orderId=${order.orderId}")
                            },
                            onChatClick = {
                                val rawMessage = userSession?.messageTemplate
                                val message = if (!rawMessage.isNullOrBlank()) rawMessage
                                else Constants.DEFAULT_TEMPLATE
                                WhatsAppHelper.launchWhatsApp(
                                    context = context,
                                    phone = order.buyerPhone,
                                    text = message
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    if (viewModel.showRecapDialog.value) {
        po?.let { dataAman ->
            RecapDialog(
                preorder = dataAman,
                onDismiss = { viewModel.showRecapDialog.value = false }
            )
        }
    }
}

@Composable
fun DetailActionItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = colorResource(id = R.color.header_card).copy(alpha = 0.1f),
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorResource(id = R.color.header_card),
                modifier = Modifier.padding(12.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun OrderListItem(
    order: OrderModel,
    isPreorderCompleted: Boolean,
    onChatClick: () -> Unit,
    onCardClick: () -> Unit
) {
    val backgroundColor = colorResource(
        id = if (isPreorderCompleted) R.color.history_preorder else R.color.preorder_card
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        color = backgroundColor,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.buyerName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Jumlah Pesanan: ${order.buyerQuantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = order.totalPrice.toRupiah(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            IconButton(
                onClick = { onChatClick() },
                modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = "Chat WhatsApp",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}