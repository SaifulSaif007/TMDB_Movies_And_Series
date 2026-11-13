package com.saiful.shared.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.NeonAmber
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowDark
import com.saiful.base.ui.theme.TextMuted
import com.saiful.shared.model.CountryWatchProviders
import com.saiful.shared.model.WatchProviderItem
import com.saiful.shared.model.WatchProvidersResponse
import com.saiful.shared.utils.AppConstants
import java.util.Locale
import androidx.core.net.toUri

@Composable
fun WatchProvidersSection(
    watchProviders: WatchProvidersResponse?,
    selectedCountry: String,
    onCountrySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val results = watchProviders?.results
    if (results.isNullOrEmpty()) return

    val availableCountries = remember(results) { results.keys.toList().sorted() }
    val effectiveCountry = if (results.containsKey(selectedCountry)) {
        selectedCountry
    } else {
        availableCountries.firstOrNull { it == "US" } ?: availableCountries.firstOrNull() ?: selectedCountry
    }

    val countryData: CountryWatchProviders? = results[effectiveCountry]
    val context = LocalContext.current
    var showCountryDialog by remember { mutableStateOf(false) }

    val countryDisplayName = remember(effectiveCountry) {
        Locale("", effectiveCountry).displayCountry.ifBlank { effectiveCountry }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // --- Header Row with Country Selector Pill ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(NeonAmber)
                )
                Text(
                    text = "Where to Watch",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceDark,
                        fontSize = 16.sp
                    )
                )
            }

            // Country Selector Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(SurfaceContainerHighDark)
                    .border(1.dp, GlassBorder, RoundedCornerShape(999.dp))
                    .clickable { showCountryDialog = true }
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = effectiveCountry,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonAmber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Country",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- Main Providers Content ---
        val streamProviders = countryData?.flatrate.orEmpty()
        val rentProviders = countryData?.rent.orEmpty()
        val buyProviders = countryData?.buy.orEmpty()
        val freeProviders = (countryData?.free.orEmpty() + countryData?.ads.orEmpty())
            .distinctBy { it.providerId }

        val hasAnyProviders = streamProviders.isNotEmpty() ||
                rentProviders.isNotEmpty() ||
                buyProviders.isNotEmpty() ||
                freeProviders.isNotEmpty()

        if (!hasAnyProviders) {
            // Empty state for this country
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerLowDark)
                    .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tv,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Not streaming in $countryDisplayName right now.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Tap the country pill above to check other regions.",
                        color = NeonAmber.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainerLowDark)
                    .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (streamProviders.isNotEmpty()) {
                    ProviderCategoryGroup(
                        title = "Stream",
                        providers = streamProviders
                    )
                }

                if (rentProviders.isNotEmpty()) {
                    ProviderCategoryGroup(
                        title = "Rent",
                        providers = rentProviders
                    )
                }

                if (buyProviders.isNotEmpty()) {
                    ProviderCategoryGroup(
                        title = "Buy",
                        providers = buyProviders
                    )
                }

                if (freeProviders.isNotEmpty()) {
                    ProviderCategoryGroup(
                        title = "Free / Ads",
                        providers = freeProviders
                    )
                }

                // JustWatch attribution and external link
                val tmdbWatchLink = countryData?.link
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Streaming data powered by JustWatch",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal
                    )

                    if (!tmdbWatchLink.isNullOrBlank()) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    openUrl(context, tmdbWatchLink)
                                }
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "View All",
                                color = CinemaCrimson,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = null,
                                tint = CinemaCrimson,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // --- Country Selection Dialog ---
    if (showCountryDialog) {
        AlertDialog(
            onDismissRequest = { showCountryDialog = false },
            title = {
                Text(
                    text = "Select Region",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = OnSurfaceDark
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(availableCountries) { countryCode ->
                        val fullName = Locale("", countryCode).displayCountry.ifBlank { countryCode }
                        val isSelected = countryCode == effectiveCountry
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) SurfaceContainerHighDark else Color.Transparent)
                                .clickable {
                                    onCountrySelected(countryCode)
                                    showCountryDialog = false
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = fullName,
                                color = if (isSelected) NeonAmber else OnSurfaceDark,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                            Text(
                                text = countryCode,
                                color = TextMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCountryDialog = false }) {
                    Text("Close", color = CinemaCrimson)
                }
            },
            containerColor = SurfaceContainerDark
        )
    }
}

@Composable
private fun ProviderCategoryGroup(
    title: String,
    providers: List<WatchProviderItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = OnSurfaceDark.copy(alpha = 0.9f),
                fontSize = 12.sp
            )
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(providers) { provider ->
                ProviderLogoCard(provider = provider)
            }
        }
    }
}

@Composable
private fun ProviderLogoCard(
    provider: WatchProviderItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(56.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val shape = RoundedCornerShape(12.dp)
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(shape)
                .background(SurfaceContainerHighDark)
                .border(1.dp, GlassBorder, shape)
        ) {
            AsyncImage(
                model = AppConstants.IMAGE_BASE_URL + "w185" + provider.logoPath,
                contentDescription = provider.providerName,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = provider.providerName ?: "",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

private fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    } catch (_: Exception) {
    }
}
