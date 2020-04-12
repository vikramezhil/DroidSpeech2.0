package github.com.vikramezhil.dks.utils

import kotlin.random.Random

/**
 * Calculation Extensions
 * @author vikramezhil
 */

/**
 * Gets a random integer
 * @receiver Random The random instance
 * @param min Int The minimum value
 * @param max Int The maximum value
 * @return Int The random integer from the range
 */
fun Random.randomInt(min: Int, max: Int): Int {
    return try {
        nextInt(min, max)
    } catch (e: Exception) {
        max
    }
}
