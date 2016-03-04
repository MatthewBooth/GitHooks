# Bolser GitHooks

### Installation

Before installing, you should:
 * check that the email address you have registered as set as your primary on GitHub is the same as YouTrack.
 * set your global git config to use the same email by issuing the following command:-

```bash
git config --global user.email "[YOUR_EMAIL_HERE]"
```

After this you can install this by running the following command from this directory (where these files are contained):-

```bash
sh install-custom-githooks.sh
```

Now all future cloned repositories will use these custom hooks

What about current repositories? Simples! Navigate to that repository and run this command:-

```bash
git init
```

### Updating

If there is an update to this repository you should navigate to where ever you cloned it to (perhaps ~/Sites/Githooks?) and run

```bash
git pull
```

Then to each of the directories that you use these hooks in, run this command

```bash
bash $(git config --path --get init.templatedir)/../update-hooks.sh
```

Updates should be few and far between, but it's worth knowing how to do this!

### Requirements

* Git
* [Java JRE 1.7 or greater](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Java JDK 1.7 or greater](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* Rsync

### How it works

This works in the following method:-

The git branch name should match the YouTrack ticket number. For example on YouTrack we use "connection-51". Therefore the git branch should contain the same. Usually something like "feature/connection-51-description-of-the-feature-or-bug".

This script will extract that and add it to the end of your commit message.

You can and should also add the time spend to the commit message in the following format:

* time:1d
* time:3h
* time:6h
* type:Testing time:1d
* type:documentation time:2h
* type:Development time:4h

You do not need to use the type parameter. It will default to "Development" if left unused. You can capitalise the word or not, the script will capitalise for you.

Add this at the end of the commit message. It should look like this

```bash
git commit -m "This is the work we did time:1d"

OR

git commit -m "This is the work we did time:1d type:documentation"
```

This will then create a git commit looking like this

```
This the work we have done

YouTrack: #connection-51 work Development 1d
```

and update YouTrack accordingly!
