package com.example.ui.theme

import androidx.compose.ui.graphics.Color

data class AppColors(
    val name: String,
    val subtitle: String,
    val desk: Color,
    val deskSurface: Color,
    val paper: Color,
    val ink: Color,
    val signalRed: Color,
    val sage: Color,
    val clay: Color,
    val mutedText: Color,
    val dividerDark: Color
)

// Legacy Fallback default colors
val Desk = Color(0xFF121417)
val DeskSurface = Color(0xFF1E2228)
val Paper = Color(0xFFF5F3EF)
val Ink = Color(0xFF0B0F19)
val SignalRed = Color(0xFFE54D2E)
val Sage = Color(0xFF8FA68E)
val Clay = Color(0xFFC9A67A)
val MutedText = Color(0xFF8C8A86)
val DividerDark = Color(0xFF2A2A2E)

val ThemePresets = listOf(
    // 0: Classic Editorial (Newsprint & Crimson)
    AppColors(
        name = "01 CLASSIC BRUTALIST",
        subtitle = "Newsprint Paper & Crimson Accent",
        desk = Color(0xFF121417),
        deskSurface = Color(0xFF1E2228),
        paper = Color(0xFFF5F3EF),
        ink = Color(0xFF0B0F19),
        signalRed = Color(0xFFE54D2E),
        sage = Color(0xFF8FA68E),
        clay = Color(0xFFC9A67A),
        mutedText = Color(0xFF8C8A86),
        dividerDark = Color(0xFF2A2A2E)
    ),
    // 1: Cyber Neon (Cyberpunk Void)
    AppColors(
        name = "02 CYBER NEON",
        subtitle = "Electric Cyan & Acid Pink",
        desk = Color(0xFF060911),
        deskSurface = Color(0xFF101726),
        paper = Color(0xFF182238),
        ink = Color(0xFF00F0FF),
        signalRed = Color(0xFFFF007A),
        sage = Color(0xFF00FF9D),
        clay = Color(0xFFFFB800),
        mutedText = Color(0xFF64748B),
        dividerDark = Color(0xFF1E293B)
    ),
    // 2: Nordic Sage (Organic Forest)
    AppColors(
        name = "03 NORDIC SAGE",
        subtitle = "Forest Sand & Moss Green",
        desk = Color(0xFF1A201C),
        deskSurface = Color(0xFF242E28),
        paper = Color(0xFFE8EBE4),
        ink = Color(0xFF121A15),
        signalRed = Color(0xFF2A6F4E),
        sage = Color(0xFF70A37F),
        clay = Color(0xFFD8B26E),
        mutedText = Color(0xFF7A8A7E),
        dividerDark = Color(0xFF2C3831)
    ),
    // 3: Retro Tokyo (City Pop Synthwave)
    AppColors(
        name = "04 RETRO TOKYO",
        subtitle = "Sunset Magenta & Electric Amber",
        desk = Color(0xFF21092B),
        deskSurface = Color(0xFF331240),
        paper = Color(0xFFFDF0F5),
        ink = Color(0xFF1C0624),
        signalRed = Color(0xFFFF2A6D),
        sage = Color(0xFF05D9E8),
        clay = Color(0xFFFFB800),
        mutedText = Color(0xFF9E7B9B),
        dividerDark = Color(0xFF451D56)
    ),
    // 4: Monochrome Noir (Stark High-Contrast B&W)
    AppColors(
        name = "05 MONOCHROME NOIR",
        subtitle = "Minimalist Stark Black & Pure White",
        desk = Color(0xFF000000),
        deskSurface = Color(0xFF141414),
        paper = Color(0xFFFFFFFF),
        ink = Color(0xFF000000),
        signalRed = Color(0xFF000000),
        sage = Color(0xFF666666),
        clay = Color(0xFF888888),
        mutedText = Color(0xFF737373),
        dividerDark = Color(0xFF262626)
    ),
    // 5: Terracotta Warmth (Mediterranean Earth)
    AppColors(
        name = "06 TERRACOTTA EARTH",
        subtitle = "Warm Clay & Olive Harvest",
        desk = Color(0xFF241814),
        deskSurface = Color(0xFF33231D),
        paper = Color(0xFFFAF0E6),
        ink = Color(0xFF22120B),
        signalRed = Color(0xFFD95D39),
        sage = Color(0xFF729B79),
        clay = Color(0xFFE09F3E),
        mutedText = Color(0xFF8D7B73),
        dividerDark = Color(0xFF3D2C25)
    ),
    // 6: Midnight Velvet (Luxury Indigo Studio)
    AppColors(
        name = "07 MIDNIGHT VELVET",
        subtitle = "Deep Indigo & Electric Violet",
        desk = Color(0xFF0B0F19),
        deskSurface = Color(0xFF151C2C),
        paper = Color(0xFFEEF2FF),
        ink = Color(0xFF0F172A),
        signalRed = Color(0xFF6366F1),
        sage = Color(0xFF10B981),
        clay = Color(0xFFF59E0B),
        mutedText = Color(0xFF64748B),
        dividerDark = Color(0xFF1E293B)
    )
)
