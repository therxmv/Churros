package com.therxmv.churros.core.design

import churros.shared.generated.resources.Res
import churros.shared.generated.resources.ic_chores_fill
import churros.shared.generated.resources.ic_chores_line
import churros.shared.generated.resources.ic_cleaning_line
import churros.shared.generated.resources.ic_cooking_line
import churros.shared.generated.resources.ic_delete_line
import churros.shared.generated.resources.ic_google
import churros.shared.generated.resources.ic_home_fill
import churros.shared.generated.resources.ic_home_line
import churros.shared.generated.resources.ic_laundry_line
import churros.shared.generated.resources.ic_notes_fill
import churros.shared.generated.resources.ic_notes_line
import churros.shared.generated.resources.ic_other_line
import churros.shared.generated.resources.ic_shopping_fill
import churros.shared.generated.resources.ic_shopping_line
import churros.shared.generated.resources.ic_visible_line
import churros.shared.generated.resources.ic_visible_off_line
import org.jetbrains.compose.resources.DrawableResource

object ChurrosIcons {
    val HomeFilled: DrawableResource = Res.drawable.ic_home_fill
    val HomeOutlined: DrawableResource = Res.drawable.ic_home_line
    val ChoresFilled: DrawableResource = Res.drawable.ic_chores_fill
    val ChoresOutlined: DrawableResource = Res.drawable.ic_chores_line
    val ShoppingFilled: DrawableResource = Res.drawable.ic_shopping_fill
    val ShoppingOutlined: DrawableResource = Res.drawable.ic_shopping_line
    val NotesFilled: DrawableResource = Res.drawable.ic_notes_fill
    val NotesOutlined: DrawableResource = Res.drawable.ic_notes_line

    // ── Chore category icons ───────────────────────────────────────────────────
    val CategoryCleaning: DrawableResource = Res.drawable.ic_cleaning_line
    val CategoryCooking: DrawableResource = Res.drawable.ic_cooking_line
    val CategoryShopping: DrawableResource = Res.drawable.ic_shopping_line
    val CategoryLaundry: DrawableResource = Res.drawable.ic_laundry_line
    val CategoryOther: DrawableResource = Res.drawable.ic_other_line

    // ── Action icons ───────────────────────────────────────────────────────────
    val Delete: DrawableResource = Res.drawable.ic_delete_line

    val EyeVisible: DrawableResource = Res.drawable.ic_visible_line
    val EyeHidden: DrawableResource = Res.drawable.ic_visible_off_line

    val GoogleLogo: DrawableResource = Res.drawable.ic_google
}
