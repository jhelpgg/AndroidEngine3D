# Regular expression

1. [Presentation](#presentation)
2. [Base expressions](#base-expressions)
3. [Repetition](#repetition)
4. [Combination](#combination)
5. [Capturing groups](#capturing-groups)
6. [Matching](#matching)
7. [Replacement](#replacement)
   1. [Basic replacement](#basic-replacement)
   2. [Replace a capturing group](#replace-a-capturing-group)
8. [Existing regular expressions](#existing-regular-expressions)
9. [Memory and performance note](#memory-and-performance-note)

### Presentation

A regular expression is a powerful tool is computing for search, match, replace text.

They can be use for know if a text is an email, or transform a format to an other.

Here it's an attempt to make them more easy to read and manipulate.

### Base expressions

Base expression to build regular expressions

| Regular expression | Description | Import |
|:---:|---|:---:|
| `"hello".regexText()` | Match exactly to given text, here **`hello`** | fr.jhelp.utilities.regex.regexText |
| 'U'.regex() | Match the given character, here **`U`** | fr.jhelp.utilities.regex.regex |
| 'U'.regexOut() | Match any character except given one | fr.jhelp.utilities.regex.regexOut |
| interval('E', 'R').regexIn() | Match any character in given interval, bounds include | <p>fr.jhelp.utilities.regex.regexIn<br/>fr.jhelp.utilities.text.interval</p> |
| interval('E', 'R').regexOut() | Match any character excepts those in given interval | <p>fr.jhelp.utilities.regex.regexOut<br/>fr.jhelp.utilities.text.interval</p> |
| WHITE_SPACE | Match a "white space" that is to say : space, tabulation, line return | fr.jhelp.utilities.regex.WHITE_SPACE |
| ANY | Match any character | fr.jhelp.utilities.regex.ANY |
| charArrayOf('+', '-').regex() | Match to one of given characters | fr.jhelp.utilities.regex.regex |
| charArrayOf('+', '-').regexOut() | Match to any character except one of given characters | fr.jhelp.utilities.regex.regexOut |

Interval can be more complex, for example, for any character in `B to F or O to T`, :

````kotlin
import fr.jhelp.utilities.regex.regexIn
import fr.jhelp.utilities.text.interval

// ...
        val regex = (interval('B', 'F') + interval('O', 'T')).regexIn()
````

### Repetition

A regular expression can be repeat we can say, by example, match to one or several **`'A'`**

| Repetition | Description | Example |
|:---:|---|:---:|
| regex.zeroOrOne() | The regex is here or not | <p>`charArrayOf('+', '-').regex().zeroOrOne() + interval('0', '9').regexIn()`<br/>To match to : +6, 5, -8, 0, ...</p> |
| regex.zeroOrMore()  | the regex is repeat zero or several times | <p>`'A'.regex().zeroOrMore()` <br/> match to empty string, or one `A`, or `AA`, or `AAA`, ...</p> |
| regex.oneOrMore()  | the regex is repeat one or several times | <p>`'A'.regex().oneOrMore()` <br/> match to `A`, or `AA`, or `AAA`, ...</p> |
| regex.atLeast(5) | The regex is reapeat at least the given number time | <p>`'A'.regex().atLeast(5)` <br/> match with `AAAAA`, `AAAAAA`, `AAAAAAA`, ...</p> |
| regex.between(3, 5) | The regex is repeat a number time in given interval | <p>`'A'.regex().between(3, 5)` <br/> will match to `AAA` `AAAA`and `AAAAA` </p> |
| regex.exactly(4) | The regex is repeat exactly th given number time | <p>'Z'.regex().exactly(4) <br/> match with `ZZZZ`</p> |

### Combination

Regular expression can be combines. to say this reggex follow by this other one, we use the `+` operator.

And to q*say this regex or this other one, we use `OR`infix method.

By example to detect an integer. An integer its no sign, `+` sing, or `-` follow by at least one digit :
````kotlin
import fr.jhelp.utilities.regex.oneOrMore
import fr.jhelp.utilities.regex.plus
import fr.jhelp.utilities.regex.regex
import fr.jhelp.utilities.regex.regexIn
import fr.jhelp.utilities.regex.zeroOrOne
import fr.jhelp.utilities.text.interval

// ...
        val integerRegex =
            charArrayOf('+', '-').regex().zeroOrOne() + interval('0', '9').regexIn().oneOrMore()
````

Other example something start with `START` or `BEGIN` follow by space, follow by upper case letters :
````kotlin
import fr.jhelp.utilities.regex.OR
import fr.jhelp.utilities.regex.WHITE_SPACE
import fr.jhelp.utilities.regex.oneOrMore
import fr.jhelp.utilities.regex.plus
import fr.jhelp.utilities.regex.regexIn
import fr.jhelp.utilities.regex.regexText
import fr.jhelp.utilities.text.interval

//...
        val integerRegex =
            ("START".regexText() OR "BEGIN".regexText()) +
            WHITE_SPACE.oneOrMore() +
            interval('A', 'Z').regexIn().oneOrMore()
````

### Capturing groups

A capturing is a regex note as important.

We capture the regex inside the group to use it on replacement, see [Replace a capturing group](#replace-a-capturing-group)

Or to be able reuse it in the regex with `same()` method.

To transform a regex to a capturing group, just use the group method : `regex.group()`

Now imagine we want match a string if it starts and ends with the same word. :
````kotlin
import fr.jhelp.utilities.regex.ANY
import fr.jhelp.utilities.regex.WHITE_SPACE
import fr.jhelp.utilities.regex.group
import fr.jhelp.utilities.regex.oneOrMore
import fr.jhelp.utilities.regex.plus
import fr.jhelp.utilities.regex.regexIn
import fr.jhelp.utilities.regex.same
import fr.jhelp.utilities.regex.zeroOrMore
import fr.jhelp.utilities.text.interval

// ...
        val firstGroup = (interval('a', 'z') + interval('A', 'Z')).regexIn().oneOrMore().group()
        val regex = firstGroup + WHITE_SPACE.oneOrMore() + ANY.zeroOrMore() + firstGroup.same()
````
`firstGroup` is the capturing group we want to capture

We use it in first time as is to get the first word. the each time we want have the same captured value, we will use the method `same()`

In te example : **`TXT something TXT`** will match since satrt and end wil same word,
but **`TXT something TED`** not match.

More details;

In both examples the group `firstGroup` capture `TXT`, then all character,
at the end, it checks if last word is same as captured by `firstGroup`

### Matching

To know if a text match a regular expression, just use **matches** method:
````kotlin
import fr.jhelp.utilities.regex.ANY
import fr.jhelp.utilities.regex.WHITE_SPACE
import fr.jhelp.utilities.regex.group
import fr.jhelp.utilities.regex.oneOrMore
import fr.jhelp.utilities.regex.plus
import fr.jhelp.utilities.regex.regexIn
import fr.jhelp.utilities.regex.same
import fr.jhelp.utilities.regex.zeroOrMore
import fr.jhelp.utilities.text.interval

//...
        val firsGroup = (interval('a', 'z') + interval('A', 'Z')).regexIn().oneOrMore().group()
        val regex = firsGroup + WHITE_SPACE.oneOrMore() + ANY.zeroOrMore() + firsGroup.same()
        println(regex.matches("TXT something TXT"))
````
Will print **`true`**

It's possible do more complex things by getting a matcher.

By example, get hours, minutes and seconds from  a string  like "12h 5m 42s":
````kotlin
import fr.jhelp.utilities.extensions.DIGIT_REGEX
import fr.jhelp.utilities.extensions.int
import fr.jhelp.utilities.regex.WHITE_SPACE
import fr.jhelp.utilities.regex.group
import fr.jhelp.utilities.regex.plus
import fr.jhelp.utilities.regex.regex
import fr.jhelp.utilities.regex.regexIn
import fr.jhelp.utilities.regex.zeroOrMore
import fr.jhelp.utilities.regex.zeroOrOne
import fr.jhelp.utilities.text.interval

// ...
        val hoursGroup = (interval('0', '2').regexIn().zeroOrOne() + DIGIT_REGEX).group()
        val minutesGroup = (interval('0', '5').regexIn().zeroOrOne() + DIGIT_REGEX).group()
        val secondsGroup = (interval('0', '5').regexIn().zeroOrOne() + DIGIT_REGEX).group()
        val timeRegex =
            hoursGroup + 'h'.regex() + WHITE_SPACE.zeroOrMore() +
            minutesGroup + 'm'.regex() + WHITE_SPACE.zeroOrMore() +
            secondsGroup + 's'.regex().zeroOrOne()
        val matcher = timeRegex.matcher("12h 5m 42s")

        if (matcher.matches())
        {
            val hours = matcher.int(hoursGroup)
            val minutes = matcher.int(minutesGroup)
            val seconds = matcher.int(secondsGroup)
            println("hours=$hours")
            println("minutes=$minutes")
            println("seconds=$seconds")
        }
````
Will print :
````
hours=12
minutes=5
seconds=42
````
The test `if (matcher.matches())` is mandatory, because it not only check if match,
but it also fill the capturing groups if match

### Replacement

##### Basic replacement

Regular expression can be used for replacement all matching part of a String.

By example, to replace all "txt" by "text" in a given String:
````kotlin
import fr.jhelp.utilities.regex.regexText

// ...
        val regex = "txt".regexText()
        println(regex.replaceAll("This txt is cool. We have wrote this txt for you", "text"))
````
Will print
````
This text is cool. We have wrote this text for you
````

##### Replace a capturing group

It's possible to use what is capture by a group inside a replacement.

By example to replace any number between parenthesis to number between brace:
````kotlin
import fr.jhelp.utilities.extensions.REAL_REGEX
import fr.jhelp.utilities.regex.group
import fr.jhelp.utilities.regex.plus
import fr.jhelp.utilities.regex.regex

// ...
        val numberGroup = REAL_REGEX.group()
        val regex = '('.regex() + numberGroup + ')'.regex()
        val replacer = regex.replacer()
            .append("[")
            .append(numberGroup)
            .append("]")
        println(replacer.replaceAll("The answer is (42), and magic number is (73)"))
````
Wil print
````
The answer is [42], and magic number is [73]
````

### Existing regular expressions

In `fr.jhelp.utilities.extensions.*` it can be find some regular expression for common usage.

We have already used some of them in this documentation, here just the actual available list:

| Name | Description |
|:---:|---|
| `DIGIT_REGEX` | Represents a digit in '0' to '9' |
| `LETTER_REGEX` | represents a lower case oor upper case letter |
| `DIGIT_LETTER_UNDERSCORE_REGEX` | Represents a digit, letter or underscore |
| `INTEGER_REGEX` | Represents an integer (Long or Int) |
| `REAL_REGEX` | Represents a real (Float or Double) |
| `VARIABLE_NAME_REGEX` | Represents recommended name for variable |
| `EMAIL_REGEX` | Represents an email address |

### Memory and performance note

In the documentation, we recreate a new regular expression each time,
because we want show how to create one.

But the most of time, a regex is used (without change) several times.
So it's no need to recreate it each time. A better is to have it in a constant.

Same remarks apply to the **Replacer** instance

The regex is computed the first time a match or a replace is done. after that it will reuse the computed value even if change later.

That's mean, any change do fater a match or a replacement, is ignored
