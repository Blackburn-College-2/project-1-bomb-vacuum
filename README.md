# Bomb Vacuum

## Setting Up Your Enviornment

Note: If using Windows, use Git Bash as a terminal.

Have the following installed:
- (Git)[https://git-scm.com/downloads]
- (Java 11)[https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot]
- Java IDE of your choice. \(Recommended: NetBeans or Intellij IDEA\)

### Adding an SSH Key to Your GitHub

Open a terminal.

#### Check for existing RSA keys.

Run `ls -al ~/.ssh`

If files `id_rsa` and `id_rsa.pub` exist, skip to **Adding an RSA Key Pair**

#### Creating an RSA Key Pair

Using the email associated with your GitHub account:

Run `ssh-keygen -t rsa -b 4096 -C "your_email@example.com"`

When prompted for input, press `ENTER` and accept the defaul arguments.

#### Adding an RSA Key to Your GitHub

1. Run `cat ~/.ssh/id_rsa.pub`
1. Highlight the output and `Right-click -> Copy` the key.
1. Go to https://github.com and sign in
1. Go to https://github.com/settings/keys
1. Click "New SSH key"
   - Give a meaningful title to the key \(Example: Rlab Wedge\)
   - Paste the key. Make sure the paste starts with `ssh-rsa` and ends with your email. Remove any trailing whitespace if it exists.

### Cloning the Repo

# Rule #1: NEVER PUSH TO MASTER

# Rule #2: Follow (Google's Java Style Guide)[https://google.github.io/styleguide/javaguide.html] (Exception: use 4-space indentation)

# Guidelines

If using Windows, use Git Bash

## Branches

### Naming Conventions

- Branch names are completely lowercase.
- Branch names SHOULD NOT start with a digit.

### Creating a Branch

1. In a terminal run: `git checkout -b <branch-name>`
   - Example: `git checkout -b basicgrid`

### Switching to a Branch

1. In a terminal run: `git checkout <branch-name>
   - Example: `git checkout basicgrid`

