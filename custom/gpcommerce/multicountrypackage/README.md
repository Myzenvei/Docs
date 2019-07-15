Documentation at:

https://wiki.hybris.com/display/ps/Multi+Country+and+Channel+Enhancements+-+Technical

## Branching Strategy and Maintenance

    ----+------- master -------------+--------------------->             \                                                    ^              \                                                  /               \--- fix/< release – fix name >----->                \	    \-- feature/-< release – feature name> ------>                  \                       `-- release/-<platform version> ------>


While the master branch contains the reference implementation with the latest functionality and is the one we maintain in-house, we store versions that belong to previous Hybris releases for historical and maintenance reasons.

### Guidelines

* The *master* is the reference branch (duh)
* It is not possible to commit directly to the *master*, every change has to be done from a branch
* Bugfix and Feature branches are kept separate:
* They store potentially specific configuration values
* They store potentially specific formats or customizations
* Fix and Feature branches merge back into *master* through pull requests. For any changes that could be used by other projects, please create a pull request and address it to the Services Portfolio team (DL CEC Services Portfolio)
* Branches can also be created for initiatives or experiments using Feature branches
* When branching a Feature, please make sure to edit this README to clarify the specifics of the initiative. Namely:
* customer name (if it’s a customer-specific feature)
* location of wiki documentation
* Hybris and any dependency versions
* any relevant information about how it differs from the master
(probably best kept in the wiki, but store here if necessary)
* If the project is long-lived or requires an update, or if an
initiative is regularly maintained, it should sync up with the *master*.
* Create appropriate tags for meaningful releases of your branch,
e.g. when doing an upgrade for a specific component.


