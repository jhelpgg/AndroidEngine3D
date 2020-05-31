# Sound manager

This module simplify the sound playing.

1. [Background sound](#background-sound)
2. [Effect sound](#effect-sound)
3. [Special sound](#special-sound)
4. [Global management](#global-management)

The [SoundManager](../main/java/fr/jhelp/sound/SoundManager.kt),
can play 3 types of sound in same time.

### Background sound

Background sound is a sound that play in loop.
In game, its generally an ambiance sound or battle fanfare.

To launch one, just call `background` method.

If a background sound already playing, it first stop and replace by this new one.

### Effect sound

Effect sound are sound played one time.
Like effect in video game (Explosion, Chest open, ...) or alert in application.

To launch one, just call `effect` method.

If an effect still playing, it first stop and replace by this new one.

### Special sound

This [Sound](../main/java/fr/jhelp/sound/Sound.kt) can be played a defined number of loop and can change volume at any time.

Only one special sound can be play a time, so if launch one while an other playing.
The other one stops and replace by the new one.

Use methods to `play`, `pause`, `resume`, `stop` and `setVolume`

### Global management

[SoundManager](../main/java/fr/jhelp/sound/SoundManager.kt) provides methods  
to able `pause`, `resume` or `stopSounds`

The method `pause`, pause all sounds.
In game its good to pause sound when application pause or on call receive while playing.

Easy way to do it, is to call `pause` method in `android.app.Activity.onPause`

And for resume sounds, use `resume` methods, by example in `android.app.Activity.onResume`

:warning:
> To free memory and exit properly, it highly recommended to call `stopSounds`
> when sounds no more need, by example in `android.app.Activity.onDestroy`
