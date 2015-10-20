# Bolser GitHooks

### Installation

To install this run the following command from this directory:-

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

* work 1d
* work 3h
* work 6h

Add this at the end of the commit message. It should look like this

```bash
git commit -m "This is the work we did work 1d"
```

This will then create a git commit looking like this

```
This the work we have done

YouTrack: #connection-51 work 1d
```

and update YouTrack accordingly!
