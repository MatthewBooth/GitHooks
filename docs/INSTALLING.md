### Installation

#### Before installing

You should:
 * check that the email address you have registered and set as your primary on GitHub is the same as YouTrack.
 * set your global git config to use the same email by issuing the following command:-

```bash
git config --global user.email "[YOUR_EMAIL_HERE]"
```

#### Then

After this you can install this by running the following command from this directory (where these files are contained):-

```bash
sh bin/install-custom-githooks.sh
```

You only need to do this once

Now all future cloned repositories will use these custom hooks

What about current repositories? Simples! Navigate to that repository and run this command:-

```bash
git init
```
