package com.io.unknow.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.io.unknow.R
import com.io.unknow.domain.util.models.ProfileParams
import com.io.unknow.presentation.ui.theme.SpaceLarge
import com.io.unknow.presentation.ui.theme.SpaceSmall
import com.io.unknow.presentation.ui.theme.TextMedium
import com.io.unknow.presentation.ui.theme.TextSmall

@Composable
fun ProfileScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.primary,
        ) {
            Text(
                text = "You name",
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(SpaceSmall)
            )
        }
        LazyColumn(
            contentPadding = PaddingValues(SpaceSmall)
        ){
            item {
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
                    ))
            }
        }
    }
}

@Composable
fun ProfileColumn(
    title: String,
    listParams: List<ProfileParams>
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.primary)
        .shadow(1.dp)
    ) {
        Text(
            text = title,
            fontSize = TextSmall,
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .padding(SpaceSmall),
        )
        listParams.forEachIndexed { index, profileParams ->
            Row(
                modifier = Modifier
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
                    text = if (profileParams.isTwoRow) "\n${profileParams.param}" else profileParams.param,
                    fontSize = TextMedium
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(SpaceLarge))
}