Type Difficulty
	Field Name$
	Field Description$
	Field AggressiveNPCs%
	Field SaveType%
	Field OtherFactors%
	Field Customizable%
	Field R%, G%, B%
	Field Realism%
End Type

Global difficulties.Difficulty[5]

Global SelectedDifficulty.Difficulty

; ~ Difficulties ID Constants
;[Block]
Const SAFE% = 0
Const EUCLID% = 1
Const KETER% = 2
Const APOLLYON% = 3
Const ESOTERIC% = 4
;[End Block]

; ~ Save Types ID Constants
;[Block]
Const SAVE_ANYWHERE% = 0
Const SAVE_ON_SCREENS% = 1
Const SAVE_ON_QUIT% = 2
Const NO_SAVES% = 3
;[End Block]

; ~ Other Factors ID Constants
;[Block]
Const EASY% = 0
Const NORMAL% = 1
Const HARD% = 2
Const EXTREME% = 3
;[End Block]

Function SetDifficultyColor%(ID%, R%, G%, B%)
	difficulties[ID]\R = R
	difficulties[ID]\G = G
	difficulties[ID]\B = B
End Function

difficulties[SAFE] = New Difficulty
difficulties[SAFE]\Name = GetLocalString("menu", "new.safe")
difficulties[SAFE]\Description = GetLocalString("msg", "diff.safe")
difficulties[SAFE]\AggressiveNPCs = False
difficulties[SAFE]\Realism = False
difficulties[SAFE]\SaveType = SAVE_ANYWHERE
difficulties[SAFE]\OtherFactors = EASY
SetDifficultyColor(SAFE, 120, 150, 50)

difficulties[EUCLID] = New Difficulty
difficulties[EUCLID]\Name = GetLocalString("menu", "new.euclid")
difficulties[EUCLID]\Description = GetLocalString("msg", "diff.euclid")
difficulties[EUCLID]\AggressiveNPCs = False
difficulties[EUCLID]\Realism = False
difficulties[EUCLID]\SaveType = SAVE_ON_SCREENS
difficulties[EUCLID]\OtherFactors = NORMAL
SetDifficultyColor(EUCLID, 200, 200, 50)

difficulties[KETER] = New Difficulty
difficulties[KETER]\Name = GetLocalString("menu", "new.keter")
difficulties[KETER]\Description = GetLocalString("msg", "diff.keter")
difficulties[KETER]\AggressiveNPCs = True
difficulties[KETER]\Realism = True
difficulties[KETER]\SaveType = SAVE_ON_QUIT
difficulties[KETER]\OtherFactors = HARD
SetDifficultyColor(KETER, 200, 50, 50)

difficulties[APOLLYON] = New Difficulty
difficulties[APOLLYON]\Name = GetLocalString("menu", "new.apollyon")
difficulties[APOLLYON]\Description = GetLocalString("msg", "diff.apollyon")
difficulties[APOLLYON]\AggressiveNPCs = True
difficulties[APOLLYON]\Realism = True
difficulties[APOLLYON]\SaveType = NO_SAVES
difficulties[APOLLYON]\OtherFactors = EXTREME
SetDifficultyColor(APOLLYON, 150, 150, 150)

difficulties[ESOTERIC] = New Difficulty
difficulties[ESOTERIC]\Name = GetLocalString("menu", "new.esoteric")
difficulties[ESOTERIC]\AggressiveNPCs = False
difficulties[ESOTERIC]\Realism = False
difficulties[ESOTERIC]\Customizable = True
difficulties[ESOTERIC]\SaveType = SAVE_ANYWHERE
difficulties[ESOTERIC]\OtherFactors = EASY
SetDifficultyColor(ESOTERIC, 200, 50, 200)

If opt\DebugMode
	SelectedDifficulty = difficulties[SAFE]
Else
	SelectedDifficulty = difficulties[EUCLID]
EndIf

;~IDEal Editor Parameters:
;~C#Blitz3D TSS