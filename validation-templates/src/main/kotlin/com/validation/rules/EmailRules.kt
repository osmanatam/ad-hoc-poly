package com.validation.rules

import arrow.Kind
import arrow.core.Tuple2
import arrow.core.nel
import com.validation.ValidationError
import com.validation.typeclass.ValidatorAE

/**
 * ------------Email Rules------------
 * Arbitrary rules can be defined anywhere outside the Rules algebra.
 */
private fun <S> ValidatorAE<S, ValidationError>.contains(email: String, needle: String): Kind<S, String> =
        if (email.contains(needle, false)) just(email)
        else raiseError(ValidationError.DoesNotContain(needle).nel())

private fun <S> ValidatorAE<S, ValidationError>.maxLength(email: String, maxLength: Int): Kind<S, String> =
        if (email.length <= maxLength) just(email)
        else raiseError(ValidationError.EmailMaxLength(maxLength).nel())

/**
 * Some rules that use the applicative syntax to validate and gather errors.
 */
fun <S> ValidatorAE<S, ValidationError>.validateEmailWithRules(email: String): Kind<S, Tuple2<String, String>> =
    tupledN(
            contains(email, "@"),
            maxLength(email, 250)
    ).handleErrorWith { raiseError(it) }
