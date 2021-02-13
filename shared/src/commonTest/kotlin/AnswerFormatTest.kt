import kotlin.test.Test
import kotlin.test.assertEquals

class AnswerFormatTest {

    @Test
    fun checkAnswerFormatting() {
        "wokeuplikethis* (feat. Lil Uzi Vert)" formatsTo "wokeuplikethis"
        "wokeuplike this* feat Lil Uzi Vert)" formatsTo "wokeuplikethis"
        "Undefeated" formatsTo "undefeated"
        "Und3feated" formatsTo "und3feated"
        "Draft Day" formatsTo "draftday"
        "Draft Day ft. Young Thug" formatsTo "draftday"
        "Showin' Off Pt.1" formatsTo "showinoffpt1"
        "679" formatsTo "679"
        "679 (feat. Monty)" formatsTo "679"
        "S&M" formatsTo "sandm"
        "S and M" formatsTo "sandm"
        "Come & Go" formatsTo "comeandgo"
        "Sandstorm" formatsTo "sandstorm"
        "DÁKITI" formatsTo "dakiti"
        "Somebody’s Problem" formatsTo "somebodysproblem"
        "Somebody's Problem" formatsTo "somebodysproblem"
    }

    @Test
    fun checkAnswerEqual() {
        "S and M" formatsEqualTo "S&M"
        "Last Friday Night" formatsEqualTo "Last Friday Night (T.G.I.F.)"
        "You Make Me Feel" formatsEqualTo "You Make Me Feel..."
    }
}

private infix fun String.formatsTo(other: String) = assertEquals(other, this.formatAnswer())
private infix fun String.formatsEqualTo(other: String) = assertEquals(this.formatAnswer(), other.formatAnswer())