# Bomb Vacuum

# Rule #1: NEVER PUSH TO MASTER

Note: If using Windows, use Git Bash as a terminal. 

In a file explorer or the desktop `Right-click -> Git Bash Here`

## [Trello Board](https://trello.com/b/2BllDNvD/bombvacuum)

## Code Style

Follow [Google's Java Style Guide](https://google.github.io/styleguide/javaguide.html) 

Exception include:
- use 4-space indentation.

The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL
NOT", "SHOULD", "SHOULD NOT", "RECOMMENDED",  "MAY", and
"OPTIONAL" in this project are to be interpreted as described 
in [RFC 2119](https://tools.ietf.org/html/rfc2119).

## Setting Up Your Enviornment

Have the following installed:
- [Git](https://git-scm.com/downloads)
- [Java 11](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)
- Java IDE of your choice. \(Recommended: NetBeans or Intellij IDEA\)

### Adding an SSH Key to Your GitHub

Open a terminal.

#### Check for existing RSA keys.

Run `ls -al ~/.ssh`

If files `id_rsa` and `id_rsa.pub` exist, skip to **Adding an RSA Key to Your GitHub**

#### Creating an RSA Key Pair

Using the email associated with your GitHub account:

Run `ssh-keygen -t rsa -b 4096 -C "your_email@example.com"`

When prompted for input, press `ENTER` and accept the default arguments.

#### Adding an RSA Key to Your GitHub

1. Run `cat ~/.ssh/id_rsa.pub`
1. Highlight the output and `Right-click -> Copy` the key.
1. Go to https://github.com and sign in
1. Go to https://github.com/settings/keys
1. Click "New SSH key".
   - Give a meaningful title to the key \(Example: Rlab Wedge\).
   - Paste the key. Make sure the paste starts with `ssh-rsa` and ends with your email. 
   Remove any leading and trailing whitespace if it exists.

### Cloning the Repo

1. Run `git clone git@github.com:Blackburn-College-2/project-1-bomb-vacuum.git`
1. Run `cd project-1-bomb-vacuum`
   - ***Note: This is the repo's root directory.***

Important Note: The first time loading the project in an IDE may take a while. Same goes for first time run.

## Branches

Make sure you change to a \(or create a new\) branch before making changes. Do not work in the `master` or `dev` branches!!!

### Naming Conventions

- Branch names are completely lowercase.
- Branch names SHOULD NOT start with a digit.

### Creating a Branch

In a terminal run: `git checkout -b <branch-name>`

Example: `git checkout -b basicgrid`

### Switching to a Branch

In a terminal run: `git checkout <branch-name>`

Example: `git checkout basicgrid`

### Pushing to a Branch

In a terminal at the repo's root directory, run the following:
1. `git add .`
1. `git commit -m "<message>"` where message explains what changes you've made since the last commit
1. `git push origin <branch-name>` tab auto-complete is your friend here!

