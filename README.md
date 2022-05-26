# btelnyy-currency
 * A currency minecraft plugin.
 * Check us out on [Discord](https://discord.gg/P22tFkjTm3)
 * Report issues on GitHub or on Discord, I don't care which.
## Commands
 If you forget the syntax, just type the command without any arguments and it will print the syntax for you.
### User commands
> `/pay <player> <amount>`
* Pay another user funds
* Permission: `btelnyy.command.pay`
> `/bal or /balance <player?>`
* Check your or others balance
* Command: `btelnyy.command.balance`
> `/withdraw <amount>`
* Create a right-clickable bank note of your funds
* Permission: `btelnyy.command.withdraw`
### Admin commands
> `/setmoney <player> <amount>`
* Set balance of a user
* Permission: `btelnyy.command.setmoney`
> `/currencyreset <player>`
* Reset a players stats (deletes data file)
* Permission: `btelnyy.command.currencyreset`
> `/togglepay <player>`
* Prevents or allows a player from paying others
* Permission: `btelnyy.command.togglepay`
> `/togglepaid <player>`
* Prevents others from paying a user
* Permission: `btelnyy.command.togglepaid`
## Config
> `global_max_money` (int)
* Does nothing, this is for future use
> `deduct_amount_precent` (int)
* How much to deduct per player death in PVP (precent)
> `currency_deduct_amount_natural` (int)
* How much to deduct per player natural death (precent)
> `currency_save_path` (String)
* What is the data folder name? Within `./plugins/btelnyy-currency/`
> `player_starting_money` (int)
* How much money should every player start with?
> `currency_symbol` (String)
* What symbol to use? e.g. $ or €
> `max_withdraw_amount` (int)
* What is the maximum amount of money that a player can withdraw in one singular interaction. Set to 0 if you want to disable it (this is the default behavior)
