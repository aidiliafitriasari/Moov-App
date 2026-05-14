package com.moov.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun RatingDialog(
    onDismiss: () -> Unit,
    onSubmit: (rating: Int, comment: String) -> Unit
) {
    var selectedRating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Give Rating",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stars
                Row {
                    for (i in 1..5) {
                        IconButton(onClick = { selectedRating = i }) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star $i",
                                tint = if (i <= selectedRating) Color(0xFFF5C518) else Color(
                                    0xFF2E2E2E
                                ),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Comment Field
                OutlinedTextField(
                    value = comment,
                    onValueChange = { if (it.length <= 250) comment = it },
                    placeholder = { Text("Tulis Ulasanmu...", color = Color(0xFF757575)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFFE50914),
                        unfocusedBorderColor = Color(0xFF2E2E2E),
                        focusedContainerColor = Color(0xFF242424),
                        unfocusedContainerColor = Color(0xFF242424)
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Character count
                Text(
                    text = "${comment.length}/250",
                    color = Color(0xFF757575),
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF2E2E2E))
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Batal", color = Color(0xFFB3B3B3), fontSize = 14.sp)
                    }

                    Button(
                        onClick = { onSubmit(selectedRating, comment) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
                    ) {
                        Text(
                            "Kirim",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}