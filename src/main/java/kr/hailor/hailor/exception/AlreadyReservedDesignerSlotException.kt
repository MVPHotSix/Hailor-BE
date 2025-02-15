package kr.hailor.hailor.exception

class AlreadyReservedDesignerSlotException :
    BaseException(
        ErrorCode.ALREADY_RESERVED_DESIGNER_SLOT,
        ErrorCode.ALREADY_RESERVED_DESIGNER_SLOT.errorMessage,
    )
