Global ItemAmount%, MaxItemAmount%
Global CarriableWeight#, MaxCarriableWeight#
Global LastItemID%

Type ItemTemplates
	Field DisplayName$
	Field Name$
	Field TempName$
	Field SoundID%
	Field Found%
	Field OBJ%, OBJPath$
	Field InvImg%, InvImg2%, InvImgPath$
	Field ImgPath$, Img%
	Field ImgWidth%, ImgHeight%
	Field IsAnim%
	Field Scale#
	Field Tex%, TexPath$
	Field Weight#
	Field MaxQuantity% = -1
	Field Quantity% = -1
	Field IsWeapon% = -1
	Field WepCaliber% = -1
	Field IsHolster% = False
	Field ISScabbard% = False
	Field IsHead% = False
	Field IsTorso% = False
	Field IsGatget% = False
	Field IsBackPack% = False
	Field IsCoveringFace% = False
	Field IsFullBody% = False
End Type

Function CreateItemTemplate.ItemTemplates(DisplayName$, Name$, TempName$, OBJPath$, InvImgPath$, ImgPath$, Scale#, SoundID%, Weight#, MaxQuantity%, TexturePath$ = "", InvImgPath2$ = "", HasAnim% = False, TexFlags% = 1)
	Local it.ItemTemplates, it2.ItemTemplates
	
	it.ItemTemplates = New ItemTemplates
	; ~ If another item shares the same object, copy it
	OBJPath = ItemsPath + OBJPath
	For it2.ItemTemplates = Each ItemTemplates
		If it2\OBJPath = OBJPath And it2\OBJ <> 0
			it\OBJ = CopyEntity(it2\OBJ)
			Exit
		EndIf
	Next
	
	If it\OBJ = 0
		If HasAnim
			it\OBJ = LoadAnimMesh_Strict(OBJPath)
			it\IsAnim = True
		Else
			it\OBJ = LoadMesh_Strict(OBJPath)
			it\IsAnim = False
		EndIf
	EndIf
	it\OBJPath = OBJPath
	
	Local Texture%
	
	If TexturePath <> ""
		If (TexturePath = ImgPath) And (FileType(ItemTexturePath + TexturePath) = 0)
			TexturePath = ItemHUDTexturePath + TexturePath
		Else
			TexturePath = ItemTexturePath + TexturePath
		EndIf
		For it2.ItemTemplates = Each ItemTemplates
			If it2\TexPath = TexturePath And it2\Tex <> 0
				Texture = it2\Tex
				If opt\Atmosphere Then TextureBlend(Texture, 5)
				Exit
			EndIf
		Next
		If Texture = 0
			If Left(TexturePath, Len(ItemHUDTexturePath)) = ItemHUDTexturePath
				Texture = GetRescaledTexture(TexturePath, TexFlags, 256, 256)
			Else
				Texture = LoadTexture_Strict(TexturePath, TexFlags)
			EndIf
			If opt\Atmosphere Then TextureBlend(Texture, 5)
			it\TexPath = TexturePath
		EndIf
		EntityTexture(it\OBJ, Texture)
		it\Tex = Texture
	EndIf
	
	it\Scale = Scale
	ScaleEntity(it\OBJ, Scale, Scale, Scale, True)
	
	; ~ If another item shares the same object, copy it
	InvImgPath = ItemINVIconPath + InvImgPath
	For it2.ItemTemplates = Each ItemTemplates
		If it2\InvImgPath = InvImgPath And it2\InvImg <> 0
			it\InvImg = it2\InvImg
			If it2\InvImg2 <> 0 Then it\InvImg2 = it2\InvImg2
			Exit
		EndIf
	Next
	If it\InvImg = 0
		it\InvImg = LoadImage_Strict(InvImgPath)
		it\InvImg = ScaleImage2(it\InvImg, MenuScale, MenuScale)
		it\InvImgPath = InvImgPath
	EndIf
	
	If InvImgPath2 <> ""
		InvImgPath2 = ItemINVIconPath + InvImgPath2
		If it\InvImg2 = 0
			it\InvImg2 = LoadImage_Strict(InvImgPath2)
			it\InvImg2 = ScaleImage2(it\InvImg2, MenuScale, MenuScale)
		EndIf
	Else
		it\InvImg2 = 0
	EndIf
	
	If ImgPath <> ""
		ImgPath = ItemHUDTexturePath + ImgPath
		it\ImgPath = ImgPath
	EndIf
	
	it\DisplayName = DisplayName
	it\TempName = TempName
	it\Name = Name
	
	it\SoundID = SoundID
	
	it\Weight = Weight
	
	it\MaxQuantity = MaxQuantity
	
	HideEntity(it\OBJ)
	
	Return(it)
End Function

Function RemoveItemTemplate(itt.ItemTemplates)
	FreeEntity(itt\OBJ) : itt\OBJ = 0
	
	FreeImage(itt\InvImg) : itt\InvImg = 0
	If itt\InvImg2 <> 0 Then FreeImage(itt\InvImg2) : itt\InvImg2 = 0
	
	If itt\Img <> 0 Then FreeImage(itt\Img) : itt\Img = 0
	
	If itt\Tex <> 0 Then DeleteSingleTextureEntryFromCache(itt\Tex) : itt\Tex = 0
	Delete(itt)
End Function

Type Items
	Field DisplayName$
	Field Name$
	Field Collider%, Model%
	Field ItemTemplate.ItemTemplates
	Field DropSpeed#
	Field R%, G%, B%, Alpha#
	Field Dist#, DistTimer#
	Field State#, State2#, State3#
	Field Picked%, Dropped%
	Field InvImg%, InvImg2%
	Field xSpeed#, zSpeed#
	Field SecondInv.Items[20]
	Field ID%
	Field InvSlots%
	Field Quantity%
End Type

Dim Inventory.Items(0)

Global SelectedItem.Items

Global ClosestItem.Items
Global OtherOpen.Items = Null

Function CreateItem.Items(Name$, TempName$, x#, y#, z#, R% = 0, G% = 0, B% = 0, Alpha# = 1.0, InvSlots% = 0)
	CatchErrors("CreateItem.Items(" + Name + ", " + TempName + ", " + x + ", " + y + ", " + z + ", " + R + ", " + G + ", " + B + ", " + Alpha + ", " + InvSlots + ")")
	
	Local i.Items, it.ItemTemplates
	
	Name = Lower(Name)
	TempName = Lower(TempName)
	
	i.Items = New Items
	For it.ItemTemplates = Each ItemTemplates
		If Lower(it\Name) = Name And Lower(it\TempName) = TempName
			i\ItemTemplate = it
			i\Collider = CreatePivot()
			EntityRadius(i\Collider, 0.01)
			EntityPickMode(i\Collider, 1, False)
			i\Model = CopyEntity(it\OBJ, i\Collider)
			i\DisplayName = it\DisplayName
			i\Name = it\Name
			ShowEntity(i\Collider)
			ShowEntity(i\Model)
			Exit
		EndIf
	Next 
	
	If i\ItemTemplate = Null Then RuntimeError(Format(Format(GetLocalString("runerr", "item"), Name, "{0}"), TempName, "{1}"))
	
	ResetEntity(i\Collider)
	PositionEntity(i\Collider, x, y, z, True)
	RotateEntity(i\Collider, 0.0, Rnd(360.0), 0.0)
	
	i\Dist = EntityDistanceSquared(me\Collider, i\Collider)
	i\DropSpeed = 0.0
	
	If TempName = "cup"
		i\R = R
		i\G = G
		i\B = B
		i\Alpha = Alpha
		i\State = 1.0
		
		Local Liquid% = CopyEntity(misc_I\CupLiquid)
		
		ScaleEntity(Liquid, i\ItemTemplate\Scale, i\ItemTemplate\Scale, i\ItemTemplate\Scale, True)
		PositionEntity(Liquid, EntityX(i\Collider, True), EntityY(i\Collider, True), EntityZ(i\Collider, True))
		EntityParent(Liquid, i\Model)
		EntityColor(Liquid, R, G, B)
		
		If Alpha < 0.0
			EntityFX(Liquid, 1)
			EntityAlpha(Liquid, Abs(Alpha))
		Else
			EntityAlpha(Liquid, Abs(Alpha))
		EndIf
		EntityShininess(Liquid, 1.0)
	EndIf
	
	i\InvImg = i\ItemTemplate\InvImg
	If i\ItemTemplate\InvImg2 <> "" Then i\InvImg2 = i\ItemTemplate\InvImg2
	If TempName = "clipboard" And InvSlots = 0
		InvSlots = 10
		SetAnimTime(i\Model, 17.0)
		i\InvImg = i\ItemTemplate\InvImg2
	ElseIf TempName = "wallet" And InvSlots = 0
		InvSlots = 10
		SetAnimTime(i\Model, 0.0)
	EndIf
	
	i\InvSlots = InvSlots
	
	If i\ItemTemplate\MaxQuantity <> -1 Then i\Quantity = 1
	
	i\ID = LastItemID + 1
	LastItemID = i\ID
	
	CatchErrors("Uncaught: CreateItem.Items(" + Name + ", " + TempName + ", " + x + ", " + y + ", " + z + ", " + R + ", " + G + ", " + B + ", " + Alpha + ", " + InvSlots + ")")
	
	Return(i)
End Function

Function RemoveItem%(i.Items)
	CatchErrors("RemoveItem()")
	
	Local n%
	
	FreeEntity(i\Model) : i\Model = 0
	FreeEntity(i\Collider) : i\Collider = 0
	
	For n = 0 To MaxItemAmount - 1
		If Inventory(n) = i
			Inventory(n) = Null
			Exit
		EndIf
	Next
	
	If SelectedItem = i Then SelectedItem = Null
	
	If i\ItemTemplate\Img <> 0 Then FreeImage(i\ItemTemplate\Img) : i\ItemTemplate\Img = 0
	Delete(i)
	
	CatchErrors("Uncaught: RemoveItem()")
End Function

Function RemoveWearableItems%(item.Items)
	Select(item\ItemTemplate\TempName)
		Case "gasmask", "gasmask2", "gasmask3", "gasmask4"
			;[Block]
			wi\GasMask = 0
			;[End Block]
		Case "nvg", "nvg2", "nvg3"
			;[Block]
			If wi\NightVision > 0 Then opt\CameraFogFar = 6.0 : wi\NightVision = 0
			;[End Block]
		Case "scramble", "scramble2"
			;[Block]
			If wi\SCRAMBLE > 0 Then opt\CameraFogFar = 6.0 : wi\SCRAMBLE = 0
			;[End Block]
		Case "helmet"
			;[Block]
			wi\BallisticHelmet = False
			;[End Block]
		Case "vest", "vest2"
			;[Block]
			wi\BallisticVest = 0
			;[End Block]
		Case "hazmat", "hazmat2", "hazmat3", "hazmat4"
			;[Block]
			wi\HazmatSuit = 0
			;[End Block]
		Case "scp268fake", "scp268", "scp268fine"
			;[Block]
			I_268\Using = 0
			;[End Block]
		Case "scp427"
			;[Block]
			I_427\Using = False
			;[End Block]
		Case "scp714", "coarse714"
			;[Block]
			I_714\Using = 0
			;[End Block]
	End Select
End Function

Function ClearSecondInv%(item.Items, From% = 0)
	Local i%
	
	For i = From To 19
		If item\SecondInv[i] <> Null Then RemoveItem(item\SecondInv[i]) : item\SecondInv[i] = Null
	Next
End Function

Function UpdateItems%()
	CatchErrors("UpdateItems()")
	
	Local i.Items, i2.Items, np.NPCs
	Local xTemp#, yTemp#, zTemp#
	Local Temp%, n%
	Local Pick%, ed#
	Local HideDist# = PowTwo(HideDistance)
	Local PushDist# = HideDist * 0.04
	Local DeletedItem% = False
	
	ClosestItem = Null
	For i.Items = Each Items
		i\Dropped = 0
		
		If (Not i\Picked)
			If i\DistTimer < MilliSecs()
				i\Dist = EntityDistanceSquared(Camera, i\Collider)
				i\DistTimer = MilliSecs() + 700
				If i\Dist < HideDist
					If EntityHidden(i\Collider) Then ShowEntity(i\Collider)
				EndIf
			EndIf
			
			If i\Dist < HideDist
				If EntityHidden(i\Collider) Then ShowEntity(i\Collider)
				If i\Dist < 1.44
					If ClosestItem = Null
						If EntityInView(i\Model, Camera) And EntityVisible(i\Collider, Camera) Then ClosestItem = i
					ElseIf ClosestItem = i Lor i\Dist < EntityDistanceSquared(Camera, ClosestItem\Collider)
						If EntityInView(i\Model, Camera) And EntityVisible(i\Collider, Camera) Then ClosestItem = i
					EndIf
				EndIf
				
				If EntityCollided(i\Collider, HIT_MAP)
					i\DropSpeed = 0.0
					i\xSpeed = 0.0
					i\zSpeed = 0.0
				Else
					If ShouldEntitiesFall
						Pick = LinePick(EntityX(i\Collider), EntityY(i\Collider), EntityZ(i\Collider), 0.0, -10.0, 0.0)
						If Pick
							i\DropSpeed = i\DropSpeed - (0.0004 * fps\Factor[0])
							TranslateEntity(i\Collider, i\xSpeed * fps\Factor[0], i\DropSpeed * fps\Factor[0], i\zSpeed * fps\Factor[0])
						Else
							i\DropSpeed = 0.0
							i\xSpeed = 0.0
							i\zSpeed = 0.0
						EndIf
					Else
						i\DropSpeed = 0.0
						i\xSpeed = 0.0
						i\zSpeed = 0.0
					EndIf
				EndIf
				
				If PlayerRoom\RoomTemplate\RoomID <> r_room2_storage
					If i\Dist < PushDist
						For i2.Items = Each Items
							If i <> i2 And (Not i2\Picked) And i2\Dist < PushDist
								xTemp = EntityX(i2\Collider, True) - EntityX(i\Collider, True)
								yTemp = EntityY(i2\Collider, True) - EntityY(i\Collider, True)
								zTemp = EntityZ(i2\Collider, True) - EntityZ(i\Collider, True)
								
								ed = PowTwo(xTemp) + PowTwo(zTemp)
								If ed < 0.07 And Abs(yTemp) < 0.25
									; ~ Items are too close together, push away
									xTemp = xTemp * (0.07 - ed)
									zTemp = zTemp * (0.07 - ed)
									
									While Abs(xTemp) + Abs(zTemp) < 0.001
										xTemp = xTemp + Rnd(-0.002, 0.002)
										zTemp = zTemp + Rnd(-0.002, 0.002)
									Wend
									
									TranslateEntity(i2\Collider, xTemp, 0.0, zTemp)
									TranslateEntity(i\Collider, -xTemp, 0.0, -zTemp)
								EndIf
							EndIf
						Next
					EndIf
				EndIf
				If EntityY(i\Collider) < -60.0
					RemoveItem(i)
					DeletedItem = True
				EndIf
			Else
				If (Not EntityHidden(i\Collider)) Then HideEntity(i\Collider)
			EndIf
		Else
			i\DropSpeed = 0.0
			i\xSpeed = 0.0
			i\zSpeed = 0.0
		EndIf
		
		If (Not DeletedItem)
			CatchErrors("Uncaught: UpdateItems(Item Name:" + Chr(34) + i\ItemTemplate\Name + Chr(34) + ")")
		Else
			CatchErrors("Uncaught: UpdateItems(Item doesn't exist anymore!)")
		EndIf
		DeletedItem = False
	Next
	
	If ClosestItem <> Null
		If HitKeyUse Then PickItem(ClosestItem)
	EndIf
End Function

Function PickItem%(item.Items)
	If MenuOpen Lor InvOpen Lor ConsoleOpen Lor I_294\Using Lor OtherOpen <> Null Lor d_I\SelectedDoor <> Null Lor SelectedScreen <> Null Lor me\Health = 0 Then Return
	
	CatchErrors("PickItem()")
	
	Local e.Events
	Local n% = 0, z%, itm%
	Local CanPickItem% = 1
	Local FullINV% = True
	
	For n = 0 To MaxItemAmount - 1
		If n < Inv_Slot_Head Lor n > Inv_Slot_Scabbard
			If Inventory(n) = Null; Lor (Inventory(n) = item And Inventory(n)\ItemTemplate\Quantity <> -1 And Inventory(n)\ItemTemplate\Quantity < Inventory(n)\ItemTemplate\MaxQuantity)
				FullINV = False
				Exit
			EndIf
		EndIf
	Next
	
	If (Not FullINV)
		For n = 0 To MaxItemAmount - 1
			If n < Inv_Slot_Head Lor n > Inv_Slot_Scabbard
				If Inventory(n) = Null; Lor (Inventory(n) = item And Inventory(n)\ItemTemplate\Quantity <> -1 And Inventory(n)\ItemTemplate\Quantity < Inventory(n)\ItemTemplate\MaxQuantity)
					Select(item\ItemTemplate\TempName)
						Case "scp1123"
							;[Block]
							Use1123()
							If I_714\Using = 0 And wi\GasMask <> 4 And wi\HazmatSuit <> 4 Then Return
							;[End Block]
						Case "killbat"
							;[Block]
							me\LightFlash = 1.0
							PlaySound_Strict(IntroSFX[Rand(8, 10)])
							msg\DeathMsg = Format(GetLocalString("death", "killbat"), Occupation)
							Kill()
							;[End Block]
						Case "scp148"
							;[Block]
							GiveAchievement(Achv148)
							;[End Block]
						Case "key6"
							;[Block]
							GiveAchievement(AchvKeyCard6)
							;[End Block]
						Case "keyomni"
							;[Block]
							GiveAchievement(AchvOmni)
							;[End Block]
						Case "scp005"
							;[Block]
							GiveAchievement(Achv005)
							;[End Block]
						Case "vest3"
							;[Block]
							CreateMsg(GetLocalString("msg", "vfvest"))
							Return
							;[End Block]
						Case "corrvest"
							;[Block]
							CreateMsg(GetLocalString("msg", "corrvest"))
							Return
							;[End Block]
						Case "firstaid", "finefirstaid", "veryfinefirstaid", "firstaid2"
							;[Block]
							item\State = 0.0
							;[End Block]
						Case "navulti"
							;[Block]
							GiveAchievement(AchvSNAV)
							;[End Block]
					End Select
					
					If item\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[item\ItemTemplate\SoundID])
					item\Picked = True
					item\Dropped = -1
					
					CarriableWeight = item\ItemTemplate\Weight
					For itm = 0 To MaxItemAmount - 1
						If Inventory(itm) <> Null
							CarriableWeight = CarriableWeight + Inventory(itm)\ItemTemplate\Weight
						EndIf
					Next
					
;					If (Inventory(n) <> Null And Inventory(n)\ItemTemplate\Quantity <> -1)
;						Inventory(n)\ItemTemplate\Quantity = Inventory(n)\ItemTemplate\Quantity + item\ItemTemplate\Quantity
;					Else
						item\ItemTemplate\Found = True
						If item\InvSlots > 0
							For z = 0 To item\InvSlots - 1
								If item\SecondInv[z] <> Null Then item\SecondInv[z]\ItemTemplate\Found = True
							Next
						EndIf
						ItemAmount = ItemAmount + 1
;					EndIf
					
					Inventory(n) = item
					HideEntity(item\Collider)
					Exit
				EndIf
			EndIf
		Next
		me\SndVolume = Max(2.0, me\SndVolume)
	Else
		CreateMsg(GetLocalString("msg", "cantcarry"))
	EndIf
	
	CatchErrors("Uncaught: PickItem()")
End Function

Function DropItem%(item.Items, PlayDropSound% = True)
	CatchErrors("DropItem()")
	
	Local n%, itm%
	Local CameraYaw# = EntityYaw(Camera)
	
	If PlayDropSound
		If item\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[item\ItemTemplate\SoundID])
	EndIf
	
	item\Dropped = 1
	
	ShowEntity(item\Collider)
	PositionEntity(item\Collider, EntityX(Camera), EntityY(Camera), EntityZ(Camera))
	RotateEntity(item\Collider, EntityPitch(Camera), CameraYaw + Rnd(-20.0, 20.0), 0.0)
	MoveEntity(item\Collider, 0.0, -0.1, 0.1)
	RotateEntity(item\Collider, 0.0, CameraYaw + Rnd(-110.0, 110.0), 0.0)
	ResetEntity(item\Collider)
	
	Local IN$ = item\ItemTemplate\TempName
	
	If IN = "hazmat" Lor IN = "hazmat2" Lor IN = "hazmat3" Lor IN = "hazmat4" Then SetAnimTime(item\Model, 4.0)
	
	item\Picked = False
	For n = 0 To MaxItemAmount - 1
		If Inventory(n) = item
			Inventory(n) = Null
			Exit
		EndIf
	Next
	ItemAmount = ItemAmount - 1
	me\SndVolume = Max(2.0, me\SndVolume)
	CarriableWeight = 0.0
	For itm = 0 To MaxItemAmount - 1
		If Inventory(itm) <> Null
			CarriableWeight = CarriableWeight + Inventory(itm)\ItemTemplate\Weight
		EndIf
	Next
	
	CatchErrors("Uncaught: DropItem()")
End Function

Function IsItemGoodFor1162ARC%(itt.ItemTemplates)
	Select(itt\TempName)
		Case "key0", "key1", "key2", "key3"
			;[Block]
			Return(True)
			;[End Block]
		Case "mastercard", "playcard", "origami", "electronics", "scp420j", "cigarette"
			;[Block]
			Return(True)
			;[End Block]
		Case "vest", "vest2", "gasmask"
			;[Block]
			Return(True)
			;[End Block]
		Case "radio", "18vradio"
			;[Block]
			Return(True)
			;[End Block]
		Case "clipboard", "eyedrops", "nvg"
			;[Block]
			Return(True)
			;[End Block]
		Default
			;[Block]
			If itt\TempName <> "paper"
				Return(False)
			ElseIf Instr(itt\Name, "Leaflet")
				Return(False)
			Else
				; ~ If the item is a paper, only allow spawning it if the name contains the word "note" or "log"
				; ~ (Because those are items created recently, which D-9341 has most likely never seen)
				Return(((Not Instr(itt\Name, "Note")) And (Not Instr(itt\Name, "Log"))))
			EndIf
			;[End Block]
	End Select
End Function

Function IsItemInFocus%()
	Select(SelectedItem\ItemTemplate\TempName)
		Case "nav", "nav300", "nav310", "navulti", "paper", "oldpaper", "badge", "oldbadge", "scp1025"
			;[Block]
			Return(True)
			;[End Block]
	End Select
	Return(False)
End Function

Function CanUseItem%(Edible% = False, RequiresBody% = False)
	
	If Edible Then
		If ((Inventory(Inv_Slot_Head) = Null Lor (Inventory(Inv_Slot_Head) <> Null And (Not Inventory(Inv_Slot_Head)\ItemTemplate\IsCoveringFace))) And (Inventory(Inv_Slot_Torso) = Null Lor (Inventory(Inv_Slot_Torso) <> Null And (Not Inventory(Inv_Slot_Torso)\ItemTemplate\IsCoveringFace)))) Then Return(True)
	Else
		Return(False)
	EndIf
	If RequiresBody Then
		If (Inventory(Inv_Slot_Torso) = Null Lor (Inventory(Inv_Slot_Torso) <> Null And (Not Inventory(Inv_Slot_Torso)\ItemTemplate\IsFullBody))) Then Return(True)
	Else
		Return(False)
	EndIf
	
End Function

; ~ SCP-914 Constants
;[Block]
Const ROUGH% = -2
Const COARSE% = -1
Const ONETOONE% = 0
Const FINE% = 1
Const VERYFINE% = 2
;[End Block]

Function Use914%(item.Items, Setting%, x#, y#, z#)
	me\RefinedItems = me\RefinedItems + 1
	
	Local it.Items, it2.Items, it3.Items, it4.Items, it5.Items, de.Decals, n.NPCs
	Local Remove% = True, i%
	Local MakeDecal% = False
	
	Select(item\ItemTemplate\TempName)
		Case "gasmask", "gasmask2", "gasmask3", "gasmask4"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					Remove = False
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("Gas Mask", "gasmask2", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("Gas Mask", "gasmask3", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "nvg", "nvg3", "nvg2"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					MakeDecal = True
					;[End Block]
				Case COARSE
					;[Block]
					it2.Items = CreateItem("Electronical Components", "electronics", x, y, z)
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("SCRAMBLE Gear", "scramble", x, y, z)
					it2\State = Rnd(0.0, 1000.0)
					;[End Block]
				Case FINE
					;[Block]
					If Rand(5) = 1
						it2.Items = CreateItem("SCRAMBLE Gear", "scramble2", x, y, z)
						it2\State = Rnd(0.0, 1000.0)
					Else
						it2.Items = CreateItem("Night Vision Goggles", "nvg2", x, y, z)
					EndIf
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("Night Vision Goggles", "nvg3", x, y, z)
					it2\State = Rnd(0.0, 1000.0)
					;[End Block]
			End Select
			;[End Block]
		Case "scramble", "scramble2"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					MakeDecal = True
					;[End Block]
				Case COARSE
					;[Block]
					it2.Items = CreateItem("Electronical Components", "electronics", x, y, z)
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Night Vision Goggles", "nvg", x, y, z)
					it2\State = Rnd(0.0, 1000.0)
					;[End Block]
				Case FINE, VERYFINE
					;[Block]
					it2.Items = CreateItem("SCRAMBLE Gear", "scramble2", x, y, z)
					it2\State = Rnd(0.0, 1000.0)
					;[End Block]
			End Select
			;[End Block]
		Case "helmet"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Ballistic Vest", "vest", x, y, z)
					;[End Block]	
				Case FINE, VERYFINE
					;[Block]
					it2.Items = CreateItem("Heavy Ballistic Vest", "vest2", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "scp268fake", "scp268", "scp268fine"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					MakeDecal = True
					;[End Block]
				Case COARSE
					;[Block]
					it2.Items = CreateItem("Newsboy Cap", "scp268fake", x, y, z)
					;[End Block]
				Case ONETOONE
					;[Block]
					Remove = False
					;[End Block]
				Case FINE, VERYFINE
					;[Block]
					it2.Items = CreateItem("SCP-268", "scp268fine", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "vest", "vest2"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					MakeDecal = True
					;[End Block]
				Case COARSE
					;[Block]
					it2.Items = CreateItem("Corrosive Ballistic Vest", "corrvest", x, y, z)
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Ballistic Helmet", "helmet", x, y, z)
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("Heavy Ballistic Vest", "vest2", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("Bulky Ballistic Vest", "vest3", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "hazmat", "hazmat2", "hazmat4"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Hazmat Suit", "hazmat", x, y, z)
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("Hazmat Suit", "hazmat2", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("Hazmat Suit", "hazmat3", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "hazmat3"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Hazmat Suit", "hazmat", x, y, z)
					;[End Block]
				Case FINE, VERYFINE
					;[Block]
					it2.Items = CreateItem("Syringe", "syringeinf", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "clipboard", "wallet"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					MakeDecal = True
					ClearSecondInv(item, 0)
					;[End Block]
				Case COARSE
					;[Block]
					If item\InvSlots > 5
						item\InvSlots = item\InvSlots - 5
						ClearSecondInv(item, item\InvSlots)
					ElseIf item\InvSlots = 5
						item\InvSlots = 1
						ClearSecondInv(item, 1)
					Else
						MakeDecal = True
						ClearSecondInv(item, 0)
					EndIf
					Remove = False
				Case ONETOONE
					;[Block]
					Remove = False
					;[End Block]
				Case FINE
					;[Block]
					If item\InvSlots = 1
						item\InvSlots = 5
					Else
						item\InvSlots = Min(20.0, item\InvSlots + 5.0)
					EndIf
					Remove = False
					;[End Block]
				Case VERYFINE
					;[Block]
					If item\InvSlots = 1
						item\InvSlots = 10
					Else
						item\InvSlots = Min(20.0, item\InvSlots + 10.0)
					EndIf
					Remove = False
					;[End Block]
			End Select
			;[End Block]
		Case "electronics"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					Remove = False
					;[End Block]
				Case FINE
					;[Block]
					Select(Rand(3))
						Case 1
							;[Block]
							it2.Items = CreateItem("Radio Transceiver", "radio", x, y, z)
							it2\State = Rnd(0.0, 100.0)
							;[End Block]
						Case 2
							;[Block]
							If Rand(3) = 1
								it2.Items = CreateItem("S-NAV 300 Navigator", "nav300", x, y, z)
							Else
								it2.Items = CreateItem("S-NAV Navigator", "nav", x, y, z)
								it2\State = Rnd(0.0, 100.0)
							EndIf
							;[End Block]
						Case 3
							;[Block]
							it2.Items = CreateItem("Night Vision Goggles", "nvg", x, y, z)
							it2\State = Rnd(0.0, 1000.0)
							;[End Block]
					End Select
					;[End Block]
				Case VERYFINE
					;[Block]
					Select(Rand(3))
						Case 1
							;[Block]
							If Rand(3) = 1
								it2.Items = CreateItem("Radio Transceiver", "fineradio", x, y, z)
							Else
								it2.Items = CreateItem("Radio Transceiver", "veryfineradio", x, y, z)
							EndIf
							;[End Block]
						Case 2
							;[Block]
							If Rand(2) = 1
								it2.Items = CreateItem("S-NAV Navigator Ultimate", "navulti", x, y, z)
							Else
								it2.Items = CreateItem("S-NAV 310 Navigator", "nav310", x, y, z)
								it2\State = Rnd(0.0, 100.0)
							EndIf
							;[End Block]
						Case 3
							;[Block]
							Select(Rand(4))
								Case 1
									;[Block]
									it2.Items = CreateItem("Night Vision Goggles", "nvg2", x, y, z)
									;[End Block]
								Case 2
									;[Block]
									it2.Items = CreateItem("Night Vision Goggles", "nvg3", x, y, z)
									it2\State = Rnd(0.0, 1000.0)
									;[End Block]
								Case 3
									;[Block]
									it2.Items = CreateItem("SCRAMBLE Gear", "scramble", x, y, z)
									it2\State = Rnd(0.0, 1000.0)
									;[End Block]
								Case 4
									;[Block]
									it2.Items = CreateItem("SCRAMBLE Gear", "scramble2", x, y, z)
									;[End Block]
							End Select
							;[End Block]
					End Select
					;[End Block]
			End Select
			;[End Block]
		Case "scp148"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					MakeDecal = True
					;[End Block]
				Case COARSE
					;[Block]
					it2.Items = CreateItem("SCP-148 Ingot", "scp148ingot", x, y, z)
				Case ONETOONE, FINE, VERYFINE
					;[Block]
					Remove = False
					;[End Block]
			End Select
			;[End Block]
		Case "scp148ingot"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					it2.Items = CreateItem("SCP-148 Ingot", "scp148ingot", x, y, z)
					;[End Block]
				Case ONETOONE, FINE, VERYFINE
					;[Block]
					For it.Items = Each Items
						If it <> item And it\Collider <> 0 And (Not it\Picked)
							If DistanceSquared(EntityX(it\Collider, True), x, EntityZ(it\Collider, True), z) < PowTwo(180.0 * RoomScale)
								Select(it\ItemTemplate\TempName)
									Case "gasmask", "gasmask2", "gasmask3"
										;[Block]
										RemoveItem(it)
										it2.Items = CreateItem("Heavy Gas Mask", "gasmask4", x, y, z)
										Exit
										;[End Block]
									Case "hazmat", "hazmat2", "hazmat3"
										;[Block]
										RemoveItem(it)
										it2.Items = CreateItem("Heavy Hazmat Suit", "hazmat4", x, y, z)
										Exit
										;[End Block]
								End Select
							EndIf
						EndIf
					Next
					
					If it2 = Null Then it2.Items = CreateItem("Metal Panel", "scp148", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "hand", "hand2", "hand3"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE, FINE, VERYFINE
					;[Block]
					If item\ItemTemplate\TempName = "hand"
						If Rand(2) = 1
							it2.Items = CreateItem("Black Severed Hand", "hand2", x, y, z)
						Else
							it2.Items = CreateItem("Yellow Severed Hand", "hand3", x, y, z)
						EndIf
					ElseIf item\ItemTemplate\TempName = "hand2"
						If Rand(2) = 1
							it2.Items = CreateItem("White Severed Hand", "hand", x, y, z)
						Else
							it2.Items = CreateItem("Yellow Severed Hand", "hand3", x, y, z)
						EndIf
					Else
						If Rand(2) = 1
							it2.Items = CreateItem("White Severed Hand", "hand", x, y, z)
						Else
							it2.Items = CreateItem("Black Severed Hand", "hand2", x, y, z)
						EndIf
					EndIf
					;[End Block]
			End Select
			;[End Block]
		Case "firstaid", "firstaid2", "finefirstaid", "veryfinefirstaid"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					If item\ItemTemplate\TempName = "firstaid"
						it2.Items = CreateItem("Blue First Aid Kit", "firstaid2", x, y, z)
					Else
						it2.Items = CreateItem("First Aid Kit", "firstaid", x, y, z)
					EndIf
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("Compact First Aid Kit", "finefirstaid", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("Strange Bottle", "veryfinefirstaid", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "key0", "key1", "key2", "key3", "key4", "key5", "key6"
			;[Block]
			Local Level% = Right(item\ItemTemplate\TempName, 1)
			
			Select(Setting)
				Case ROUGH
					;[Block]
					MakeDecal = True
					;[End Block]
				Case COARSE
					;[Block]
					If Level = 0
						MakeDecal = True
					ElseIf Level = 6
						it2.Items = CreateItem("Level 0 Key Card", "key0", x, y, z)
					Else
						it2.Items = CreateItem("Level " + (Level - 1) + " Key Card", "key" + (Level - 1), x, y, z)
					EndIf
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Playing Card", "playcard", x, y, z)
					;[End Block]
				Case FINE
					;[Block]
					Select(Level)
						Case 0
							;[Block]
							Select(SelectedDifficulty\OtherFactors)
								Case EASY
									;[Block]
									it2.Items = CreateItem("Level 1 Key Card", "key1", x, y, z)
									;[End Block]
								Case NORMAL
									;[Block]
									If Rand(6) = 1
										it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
									Else
										it2.Items = CreateItem("Level 1 Key Card", "key1", x, y, z)
									EndIf
									;[End Block]
								Case HARD
									;[Block]
									If Rand(5) = 1
										it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
									Else
										it2.Items = CreateItem("Level 1 Key Card", "key1", x, y, z)
									EndIf
									;[End Block]
								Case EXTREME
									;[Block]
									If Rand(4) = 1
										it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
									Else
										it2.Items = CreateItem("Level 1 Key Card", "key1", x, y, z)
									EndIf
									;[End Block]
							End Select
							;[End Block]
						Case 1
							;[Block]
							Select(SelectedDifficulty\OtherFactors)
								Case EASY
									;[Block]
									it2.Items = CreateItem("Level 2 Key Card", "key2", x, y, z)
									;[End Block]
								Case NORMAL
									;[Block]
									If Rand(5) = 1
										it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
									Else
										it2.Items = CreateItem("Level 2 Key Card", "key2", x, y, z)
									EndIf
									;[End Block]
								Case HARD
									;[Block]
									If Rand(4) = 1
										it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
									Else
										it2.Items = CreateItem("Level 2 Key Card", "key2", x, y, z)
									EndIf
									;[End Block]
								Case EXTREME
									;[Block]
									If Rand(3) = 1
										it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
									Else
										it2.Items = CreateItem("Level 2 Key Card", "key2", x, y, z)
									EndIf
									;[End Block]
							End Select
							;[End Block]
						Case 2
							;[Block]
							Select(SelectedDifficulty\OtherFactors)
								Case EASY
									;[Block]
									it2.Items = CreateItem("Level 3 Key Card", "key3", x, y, z)
									;[End Block]
								Case NORMAL
									;[Block]
									If Rand(4) = 1
										it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
									Else
										it2.Items = CreateItem("Level 3 Key Card", "key3", x, y, z)
									EndIf
									;[End Block]
								Case HARD
									;[Block]
									If Rand(3) = 1
										it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
									Else
										it2.Items = CreateItem("Level 3 Key Card", "key3", x, y, z)
									EndIf
									;[End Block]
								Case EXTREME
									;[Block]
									If Rand(2) = 1
										it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
									Else
										it2.Items = CreateItem("Level 3 Key Card", "key3", x, y, z)
									EndIf
									;[End Block]
							End Select
							;[End Block]
						Case 3
							;[Block]
							If Rand(12 + (6 * SelectedDifficulty\OtherFactors)) = 1
								it2.Items = CreateItem("Level 4 Key Card", "key4", x, y, z)
							Else
								it2.Items = CreateItem("Playing Card", "playcard", x, y, z)
							EndIf
							;[End Block]
						Case 4
							;[Block]
							Select(SelectedDifficulty\OtherFactors)
								Case EASY
									;[Block]
									it2.Items = CreateItem("Level 5 Key Card", "key5", x, y, z)
									;[End Block]
								Case NORMAL
									;[Block]
									If Rand(4) = 1
										it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
									Else
										it2.Items = CreateItem("Level 5 Key Card", "key5", x, y, z)
									EndIf
									;[End Block]
								Case HARD
									;[Block]
									If Rand(3) = 1
										it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
									Else
										it2.Items = CreateItem("Level 5 Key Card", "key5", x, y, z)
									EndIf
									;[End Block]
								Case EXTREME
									;[Block]
									If Rand(2) = 1
										it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
									Else
										it2.Items = CreateItem("Level 5 Key Card", "key5", x, y, z)
									EndIf
									;[End Block]
							End Select
							;[End Block]
						Case 5
							;[Block]
							Local CurrAchvAmount% = 0
							
							For i = 0 To MaxAchievements - 1
								If achv\Achievement[i] = True Then CurrAchvAmount = CurrAchvAmount + 1
							Next
							
							If Rand(0, ((MaxAchievements - 1) * (2 + SelectedDifficulty\OtherFactors)) - ((CurrAchvAmount - 1) * (2 + SelectedDifficulty\OtherFactors))) = 0
								it2.Items = CreateItem("Key Card Omni", "keyomni", x, y, z)
							Else
								If Rand(12 + (6 * SelectedDifficulty\OtherFactors)) = 1
									it2.Items = CreateItem("Level 6 Key Card", "key6", x, y, z)
								Else
									it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
								EndIf
							EndIf
							;[End Block]
						Case 6
							;[Block]
							If Rand(6 + (3 * SelectedDifficulty\OtherFactors)) = 1
								it2.Items = CreateItem("Key Card Omni", "keyomni", x, y, z)
							Else
								it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
							EndIf
							;[End Block]
					End Select
					;[End Block]
				Case VERYFINE
					;[Block]
					CurrAchvAmount = 0
					For i = 0 To MaxAchievements - 1
						If achv\Achievement[i] = True Then CurrAchvAmount = CurrAchvAmount + 1
					Next
					
					If Rand(0, ((MaxAchievements - 1) * (4 + SelectedDifficulty\OtherFactors)) - ((CurrAchvAmount - 1) * (4 + SelectedDifficulty\OtherFactors))) = 0
						it2.Items = CreateItem("Key Card Omni", "keyomni", x, y, z)
					Else
						If Rand(24 + (6 * SelectedDifficulty\OtherFactors)) = 1
							it2.Items = CreateItem("Level 6 Key Card", "key6", x, y, z)
						Else
							it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
						EndIf
					EndIf
					;[End Block]
			End Select
			;[End Block]
		Case "keyomni"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					If Rand(2) = 1
						it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
					Else
						it2.Items = CreateItem("Playing Card", "playcard", x, y, z)
					EndIf
					;[End Block]
				Case FINE, VERYFINE
					;[Block]
					If Rand(4 + (2 * SelectedDifficulty\OtherFactors)) = 1
						it2.Items = CreateItem("Level 6 Key Card", "key6", x, y, z)
					Else
						it2.Items = CreateItem("Mastercard", "mastercard", x, y, z)
					EndIf
					;[End Block]
			End Select
			;[End Block]
		Case "playcard", "coin", "25ct"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Level 0 Key Card", "key0", x, y, z)
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("Level 1 Key Card", "key1", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("Level 2 Key Card", "key2", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "mastercard"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					MakeDecal = True
					;[End Block]
				Case COARSE
					;[Block]
					it2.Items = CreateItem("Quarter", "25ct", x, y, z)
					
					If Rand(2) = 1
						it3.Items = CreateItem("Quarter", "25ct", x, y, z)
						EntityType(it3\Collider, HIT_ITEM)
					EndIf
					
					If Rand(3) = 1
						it4.Items = CreateItem("Quarter", "25ct", x, y, z)
						EntityType(it4\Collider, HIT_ITEM)
					EndIf
					
					If Rand(4) = 1
						it5.Items = CreateItem("Quarter", "25ct", x, y, z)
						EntityType(it5\Collider, HIT_ITEM)
					EndIf
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Level 0 Key Card", "key0", x, y, z)
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("Level 1 Key Card", "key1", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("Level 2 Key Card", "key2", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "radio", "18vradio", "fineradio", "veryfineradio"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					MakeDecal = True
					;[End Block]
				Case COARSE
					;[Block]
					it2.Items = CreateItem("Electronical Components", "electronics", x, y, z)
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Radio Transceiver", "18vradio", x, y, z)
					it2\State = Rnd(0.0, 100.0)
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("Radio Transceiver", "fineradio", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("Radio Transceiver", "veryfineradio", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "nav", "nav300", "nav310", "navulti"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					MakeDecal = True
					;[End Block]
				Case COARSE
					;[Block]
					it2.Items = CreateItem("Electronical Components", "electronics", x, y, z)
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("S-NAV Navigator", "nav", x, y, z)
					it2\State = Rnd(0.0, 100.0)
					;[End Block]
				Case FINE
					;[Block]
					If Rand(3) = 1
						it2.Items = CreateItem("S-NAV 300 Navigator", "nav300", x, y, z)
					Else
						it2.Items = CreateItem("S-NAV 310 Navigator", "nav310", x, y, z)
						it2\State = Rnd(0.0, 100.0)
					EndIf
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("S-NAV Navigator Ultimate", "navulti", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "scp513"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					PlaySound_Strict(LoadTempSound("SFX\SCP\513\914Refine.ogg"))
					For n.NPCs = Each NPCs
						If n\NPCType = NPCType513_1 Then RemoveNPC(n)
					Next
					MakeDecal = True
					;[End Block]
				Case ONETOONE, FINE, VERYFINE
					;[Block]
					it2.Items = CreateItem("SCP-513", "scp513", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "scp420j", "cigarette", "joint", "scp420s"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Cigarette", "cigarette", x + 1.5, y + 0.5, z + 1.0)
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("Joint", "joint", x + 1.5, y + 0.5, z + 1.0)
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("Smelly Joint", "scp420s", x + 1.5, y + 0.5, z + 1.0)
					;[End Block]
			End Select
			;[End Block]
		Case "scp714", "coarse714", "fine714", "ring"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					MakeDecal = True
					;[End Block]
				Case COARSE
					;[Block]
					it2.Items = CreateItem("SCP-714", "coarse714", x, y, z)
					;[End Block]
				Case ONETOONE
					;[Block]
					If item\ItemTemplate\TempName = "scp714"
						it2.Items = CreateItem("Green Jade Ring", "ring", x, y, z)
					Else
						it2.Items = CreateItem("SCP-714", "scp714", x, y, z)
					EndIf
					;[End Block]
				Case FINE, VERYFINE
					;[Block]
					it2.Items = CreateItem("SCP-714", "fine714", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "coarsebat"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					Remove = False
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("9V Battery", "bat", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("18V Battery", "finebat", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "bat"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					MakeDecal = True
					;[End Block]
				Case COARSE
					;[Block]
					it2.Items = CreateItem("4.5V Battery", "coarsebat", x, y, z)
					;[End Block]
				Case ONETOONE
					;[Block]
					Remove = False
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("18V Battery", "finebat", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					If Rand(5) = 1
						it2.Items = CreateItem("999V Battery", "veryfinebat", x, y, z)
					Else
						it2.Items = CreateItem("Strange Battery", "killbat", x, y, z)
					EndIf
					;[End Block]
			End Select
			;[End Block]
		Case "finebat"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					it2.Items = CreateItem("4.5V Battery", "coarsebat", x, y, z)
					;[End Block]
				Case COARSE
					;[Block]
					it2.Items = CreateItem("9V Battery", "bat", x, y, z)
					;[End Block]
				Case ONETOONE
					;[Block]
					Remove = False
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("18V Battery", "finebat", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					If Rand(3) = 1
						it2.Items = CreateItem("999V Battery", "veryfinebat", x, y, z)
					Else
						it2.Items = CreateItem("Strange Battery", "killbat", x, y, z)
					EndIf
					;[End Block]
			End Select
			;[End Block]
		Case "veryfinebat"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					it2.Items = CreateItem("4.5V Battery", "coarsebat", x, y, z)
					;[End Block]
				Case COARSE
					;[Block]
					it2.Items = CreateItem("9V Battery", "bat", x, y, z)
					;[End Block]
				Case ONETOONE, FINE, VERYFINE
					;[Block]
					it2.Items = CreateItem("Strange Battery", "killbat", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "eyedrops", "eyedrops2", "fineeyedrops", "veryfineeyedrops"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					If Rand(2) = 1
						it2.Items = CreateItem("ReVision Eyedrops", "eyedrops", x, y, z)
					Else
						it2.Items = CreateItem("RedVision Eyedrops", "eyedrops2", x, y, z)
					EndIf
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("Eyedrops", "fineeyedrops", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("Eyedrops", "veryfineeyedrops", x, y, z)
					;[End Block]
			End Select
			;[End Block]
		Case "syringe"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Compact First Aid Kit", "finefirstaid", x, y, z)
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("Syringe", "finesyringe", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					If Rand(3) = 1
						it2.Items = CreateItem("Syringe", "veryfinesyringe", x, y, z)
					Else
						it2.Items = CreateItem("Syringe", "syringeinf", x, y, z)
					EndIf
					;[End Block]
			End Select
			;[End Block]
		Case "finesyringe"
			;[Block]
			Select(Setting)
				Case ROUGH
					;[Block]
					MakeDecal = True
					;[End Block]
				Case COARSE
					;[Block]
					it2.Items = CreateItem("First Aid Kit", "firstaid", x, y, z)
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Blue First Aid Kit", "firstaid2", x, y, z)
					;[End Block]
				Case FINE, VERYFINE
					;[Block]
					If Rand(3) = 1
						it2.Items = CreateItem("Syringe", "veryfinesyringe", x, y, z)
					Else
						it2.Items = CreateItem("Syringe", "syringeinf", x, y, z)
					EndIf
					;[End Block]
			End Select
			;[End Block]
		Case "veryfinesyringe"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE, ONETOONE
					;[Block]
					it2.Items = CreateItem("Electronical Components", "electronics", x, y, z)
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("Syringe", "syringeinf", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					If Rand(2) = 1
						n.NPCs = CreateNPC(NPCType008_1, x, y, z)
						n\State = 2.0
					Else
						it2.Items = CreateItem("Syringe", "syringeinf", x, y, z)
					EndIf
					;[End Block]
			End Select
		Case "syringeinf"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					n.NPCs = CreateNPC(NPCType008_1, x, y, z)
					n\State = 2.0
					;[End Block]	
				Case FINE
					;[Block]
					it2.Items = CreateItem("Syringe", "syringe", x, y, z)
					;[End Block]
				Case VERYFINE
					;[Block]
					If Rand(4) = 1
						it2.Items = CreateItem("Blue First Aid Kit", "firstaid2", x, y, z)
					Else
						it2.Items = CreateItem("Syringe", "finesyringe", x, y, z)
					EndIf
					;[End Block]
			End Select
		Case "scp500pill", "scp500pilldeath", "pill"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Pill", "pill", x, y, z)
					;[End Block]
				Case FINE
					;[Block]
					Local NO427Spawn% = False
					
					For it3.Items = Each Items
						If it3\ItemTemplate\TempName = "scp427"
							NO427Spawn = True
							Exit
						EndIf
					Next
					If (Not NO427Spawn)
						it2.Items = CreateItem("SCP-427", "scp427", x, y, z)
					Else
						it2.Items = CreateItem("Upgraded Pill", "scp500pilldeath", x, y, z)
					EndIf
					;[End Block]
				Case VERYFINE
					;[Block]
					If Rand(10) = 1
						it2.Items = CreateItem("SCP-500", "scp500", x, y, z)
					Else
						it2.Items = CreateItem("Upgraded Pill", "scp500pilldeath", x, y, z)
					EndIf
					;[End Block]
			End Select
			;[End Block]
		Case "scp500"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("SCP-500-01", "scp500pill", x, y, z)
					
					If Rand(2) = 1
						it3.Items = CreateItem("SCP-500-01", "scp500pill", x, y, z)
						EntityType(it3\Collider, HIT_ITEM)
					EndIf
					
					If Rand(3) = 1
						it4.Items = CreateItem("SCP-500-01", "scp500pill", x, y, z)
						EntityType(it4\Collider, HIT_ITEM)
					EndIf
					
					If Rand(4) = 1
						it5.Items = CreateItem("SCP-500-01", "scp500pill", x, y, z)
						EntityType(it5\Collider, HIT_ITEM)
					EndIf
					;[End Block]
				Case FINE
					;[Block]
					NO427Spawn = False
					
					For it3.Items = Each Items
						If it3\ItemTemplate\TempName = "scp427"
							NO427Spawn = True
							Exit
						EndIf
					Next
					If (Not NO427Spawn)
						it2.Items = CreateItem("SCP-427", "scp427", x, y, z)
					Else
						it2.Items = CreateItem("Upgraded Pill", "scp500pilldeath", x, y, z)
					EndIf
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("Upgraded Pill", "scp500pilldeath", x, y, z)
					
					If Rand(2) = 1
						it3.Items = CreateItem("Upgraded Pill", "scp500pilldeath", x, y, z)
						EntityType(it3\Collider, HIT_ITEM)
					EndIf
					
					If Rand(3) = 1
						it4.Items = CreateItem("Upgraded Pill", "scp500pilldeath", x, y, z)
						EntityType(it4\Collider, HIT_ITEM)
					EndIf
					
					If Rand(4) = 1
						it5.Items = CreateItem("Upgraded Pill", "scp500pilldeath", x, y, z)
						EntityType(it5\Collider, HIT_ITEM)
					EndIf
					;[End Block]
			End Select
			;[End Block]
		Case "cup"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					it2.Items = CreateItem("Cup", "cup", x, y, z, 255.0 - item\R, 255.0 - item\G, 255.0 - item\B)
					it2\Name = item\Name
					it2\State = item\State
					;[End Block]
				Case FINE
					;[Block]
					it2.Items = CreateItem("Cup", "cup", x, y, z, Min(item\R * Rnd(0.9, 1.1), 255.0), Min(item\G * Rnd(0.9, 1.1), 255.0), Min(item\B * Rnd(0.9, 1.1), 255.0))
					it2\Name = item\Name
					it2\State = item\State + 1.0
					;[End Block]
				Case VERYFINE
					;[Block]
					it2.Items = CreateItem("Cup", "cup", x, y, z, Min(item\R * Rnd(0.5, 1.5), 255.0), Min(item\G * Rnd(0.5, 1.5), 255.0), Min(item\B * Rnd(0.5, 1.5), 255.0))
					it2\Name = item\Name
					it2\State = item\State * 2.0
					If Rand(5) = 1 Then me\ExplosionTimer = 135.0
					;[End Block]
			End Select
			;[End Block]
		Case "scp1025"
			;[Block]
			Remove = False
			Select(Setting)
				Case ROUGH
					;[Block]
					If item\State2 > 0.0
						item\State2 = -1.0
					Else
						MakeDecal = True
						Remove = True
					EndIf
					;[End Block]
				Case COARSE
					;[Block]
					item\State2 = Max(-1.0, item\State2 - 1.0)
					;[End Block]
				Case ONETOONE
					;[Block]
					Remove = True
					it2.Items = CreateItem("Book", "book", x, y, z)
					;[End Block]
				Case FINE
					;[Block]
					item\State2 = Min(1.0, item\State2 + 1.0)
					;[End Block]
				Case VERYFINE
					;[Block]
					item\State2 = 2.0
					;[End Block]
			End Select
			;[End Block]
		Case "book"
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE
					;[Block]
					If Rand(3) = 1
						it2.Items = CreateItem("SCP-1025", "scp1025", x, y, z) ; ~ I know that this can be exploited to get a SCP-1025 reset, but this effort makes it seem fair to me -- Salvage
					Else
						Remove = False
					EndIf
					;[End Block]
				Case FINE, VERYFINE
					;[Block]
					Remove = False
					;[End Block]
			End Select
			;[End Block]
		Default
			;[Block]
			Select(Setting)
				Case ROUGH, COARSE
					;[Block]
					MakeDecal = True
					;[End Block]
				Case ONETOONE, FINE, VERYFINE
					;[Block]
					Remove = False
					;[End Block]
			End Select
			;[End Block]
	End Select
	
	If MakeDecal
		de.Decals = CreateDecal(DECAL_CORROSIVE_1, x, 8.0 * RoomScale + 0.005, z, 90.0, Rnd(360.0), 0.0, Rnd(0.3, 0.8), Rnd(0.8, 1.0), 1)
		EntityParent(de\OBJ, PlayerRoom\OBJ)
	EndIf
	If Remove
		RemoveItem(item)
	Else
		PositionEntity(item\Collider, x, y, z)
		ResetEntity(item\Collider)
	EndIf
	
	If it2 <> Null Then EntityType(it2\Collider, HIT_ITEM)
End Function

; ~ Made a function for SCP-1123 so we don't have to use duplicate code (since picking it up and using it does the same thing)
Function Use1123%()
	; ~ Temp:
	; ~		False: Stay alive
	; ~		True: Die
	
	Local e.Events
	Local Temp%
	
	If I_714\Using = 0 And wi\GasMask <> 4 And wi\HazmatSuit <> 4
		me\LightFlash = 3.0
		PlaySound_Strict(LoadTempSound("SFX\SCP\1123\Touch.ogg"))
		
		Temp = True
		For e.Events = Each Events
			If e\EventID = e_cont2_1123
				If PlayerRoom = e\room
					If e\EventState < 1.0 ; ~ Start the event
						e\EventState = 1.0
						Temp = False
						Exit
					EndIf
				EndIf
			EndIf
		Next
	Else
		CreateMsg(GetLocalString("msg", "skull.nothappend"))
	EndIf
	
	If Temp
		msg\DeathMsg = Format(GetLocalString("death", "1123"), Occupation)
		Kill()
	EndIf
End Function

; ~ Key Items Constants
;[Block]
Const KEY_CARD_6% = 1
Const KEY_CARD_0% = 2
Const KEY_CARD_1% = 3
Const KEY_CARD_2% = 4
Const KEY_CARD_3% = 5
Const KEY_CARD_4% = 6
Const KEY_CARD_5% = 7
Const KEY_CARD_OMNI% = 8

Const KEY_005% = 9

Const KEY_HAND_WHITE% = -1
Const KEY_HAND_BLACK% = -2
Const KEY_HAND_YELLOW% = -3

Const KEY_860% = -4

Const KEY_MISC% = 0
;[End Block]

; ~ Only for "UseDoor" function
Function GetUsingItem%(item.Items)
	Select(item\ItemTemplate\TempName)
		Case "key6"
			;[Block]
			Return(KEY_CARD_6)
		Case "key0"
			;[Block]
			Return(KEY_CARD_0)
			;[End Block]
		Case "key1"
			;[Block]
			Return(KEY_CARD_1)
			;[End Block]
		Case "key2"
			;[Block]
			Return(KEY_CARD_2)
			;[End Block]
		Case "key3"
			;[Block]
			Return(KEY_CARD_3)
			;[End Block]
		Case "key4"
			;[Block]
			Return(KEY_CARD_4)
			;[End Block]
		Case "key5"
			;[Block]
			Return(KEY_CARD_5)
			;[End Block]
		Case "keyomni"
			;[Block]
			Return(KEY_CARD_OMNI)
			;[End Block]
		Case "scp005"
			;[Block]
			Return(KEY_005)
			;[End Block]
		Case "hand"
			;[Block]
			Return(KEY_HAND_WHITE)
			;[End Block]
		Case "hand2"
			;[Block]
			Return(KEY_HAND_BLACK)
			;[End Block]
		Case "hand3"
			;[Block]
			Return(KEY_HAND_YELLOW)
			;[End Block]
		Case "scp860"
			;[Block]
			Return(KEY_860)
			;[End Block]
		Default
			;[Block]
			Return(KEY_MISC)
			;[End Block]
	End Select
End Function

Function CreateRandomBattery.Items(x#, y#, z#)
	Local BatteryName$, BatteryTempName$
	Local BatteryChance%, RandomChance%
	
	Select(SelectedDifficulty\OtherFactors)
		Case SAFE
			;[Block]
			BatteryChance = 10
			;[End Block]
		Case NORMAL
			;[Block]
			BatteryChance = 15
			;[End Block]
		Case HARD
			;[Block]
			BatteryChance = 20
			;[End Block]
		Case EXTREME
			;[Block]
			BatteryChance = 25
			;[End Block]
	End Select
	
	RandomChance = Rand(BatteryChance)
	If RandomChance >= 1 And RandomChance <= 7
		BatteryName = "9V Battery"
		BatteryTempName = "bat"
	ElseIf RandomChance >= 8 And RandomChance < BatteryChance
		BatteryName = "4.5V Battery"
		BatteryTempName = "coarsebat"
	Else
		BatteryName = "18V Battery"
		BatteryTempName = "finebat"
	EndIf
	Return(CreateItem(BatteryName, BatteryTempName, x, y, z))
End Function

Function IsItemInInventory(it.Items)
	Local i%, j%
	
	For i = 0 To MaxItemAmount - 1
		If Inventory(i) <> Null
			If it = Inventory(i)
				Return(True)
			ElseIf Inventory(i)\InvSlots > 0
				For j = 0 To Inventory(i)\InvSlots - 1
					If it = Inventory(i)\SecondInv[j]
						Return(True)
					EndIf
				Next
			EndIf
		EndIf
	Next
	
	Return(False)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS