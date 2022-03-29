package com.io.unknow.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.io.unknow.R
import com.io.unknow.presentation.profile.dialog.DialogChangeParams
import com.io.unknow.presentation.profile.model.ProfileParams
import com.io.unknow.presentation.ui.theme.*

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel(),
    heightToolbar: Dp = 50.dp
){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = heightToolbar),
            backgroundColor = MaterialTheme.colors.primary,
            elevation = 0.dp
        ) {
            Text(
                text = "You name",
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(SpaceSmall)
            )
        }
        Spacer(modifier = Modifier
            .height(Shadow)
            .background(Color.Black)
        )
        LazyColumn(
            contentPadding = PaddingValues(SpaceSmall)
        ){
            item {
                Spacer(modifier = Modifier.height(SpaceMedium))
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = stringResource(id = R.string.logo),
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary)
                        .shadow(1.dp)
                )
                Spacer(modifier = Modifier.height(SpaceLarge))
            }
            item {
                ProfileColumn(
                    title = stringResource(id = R.string.sex_age),
                    listParams = listOf(
                        ProfileParams(
                            title = stringResource(id = R.string.sex),
                            param = "Men",
                        ),
                        ProfileParams(
                            title = stringResource(id = R.string.age),
                            param = "19 years",
                        )
                    ))
            }
            item {
                ProfileColumn(
                    title = stringResource(id = R.string.weight_height_locate),
                    listParams = listOf(
                        ProfileParams(
                            title = stringResource(id = R.string.height),
                            param = "154 cm",
                        ),
                        ProfileParams(
                            title = stringResource(id = R.string.weight),
                            param = "62 kg",
                        ),
                        ProfileParams(
                            title = stringResource(id = R.string.location),
                            param = "Brest",
                            isTwoRow = true
                        )
                    ),
                    isEditMod = true,
                    onEdit = {

                    }
                )
            }
        }
    }
}

@Composable
fun ProfileColumn(
    title: String,
    listParams: List<ProfileParams>,
    isEditMod: Boolean = false,
    onEdit:(List<ProfileParams>) -> Unit = {}
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.primary)
        .shadow(1.dp)
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxSize()
                .padding(SpaceSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = TextSmall,
                color = Color.White
            )
            if (isEditMod) {
                var showDialog by remember {
                    mutableStateOf(false)
                }
                if (showDialog){
                    DialogChangeParams(
                        listParams = listParams,
                        onChangeParams = onEdit,
                        onClose = {
                            showDialog = !showDialog
                        }
                    )
                }
                IconButton(onClick = {
                    showDialog = !showDialog
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit),
                        tint = Color.White
                    )
                }
            }
        }
        listParams.forEachIndexed { index, profileParams ->
            if (profileParams.isTwoRow){
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(SpaceSmall)
                ) {
                    Text(
                        text = profileParams.title,
                        fontSize = TextMedium
                    )
                    Text(
                        text = profileParams.param,
                        fontSize = TextMedium,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxSize()
                        .padding(SpaceSmall),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = profileParams.title,
                        fontSize = TextMedium
                    )
                    Text(
                        text = profileParams.param,
                        fontSize = TextMedium
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(SpaceLarge))
}