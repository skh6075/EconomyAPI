package me.skh6075.dev.economyapi.transaction

enum class EconomyTransactionResult(val reason: String) {
    SUCCESS(""),
    NOT_LOADED("해당 플레이어는 오프라인입니다."),
    NOT_ENOUGH_MONEY("돈이 부족합니다."),
    PLUGIN_CANCELLED("알 수 없는 이유로 취소되었습니다."),
    NOT_ALLOWED("해당 통화는 거래할 수 없습니다.")
}