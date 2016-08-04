#!/bin/sh

echo "pre install"

/usr/bin/getent group holdmail || /usr/sbin/groupadd -r holdmail
/usr/bin/getent passwd holdmail || /usr/sbin/useradd -r -d /opt/holdmail -g holdmail -s /sbin/nologin holdmail


