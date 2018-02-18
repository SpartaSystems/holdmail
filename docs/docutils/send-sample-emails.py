#!/usr/bin/env python
# coding=utf-8

################################################################################
# Copyright 2018 Sparta Systems, Inc
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
# implied.
#
# See the License for the specific language governing permissions and
# limitations under the License.
################################################################################

# Connects to localhost:25000 and sends 100 sample emails

import email
import random
import smtplib
from email import encoders

# external dep - pip install faker
from faker import Faker

fake = Faker('en_US')
num_mails = 100


def main():
    mailer = smtplib.SMTP('localhost', 25000)

    for _ in range(num_mails):
        sender = gen_user()
        recip = gen_user()
        subject = gen_subject()
        send_mail(mailer, sender, recip, subject, random.random() < 0.4)

    mailer.close()


def gen_user():
    domains = ['gmail.com', 'hotmail.com', 'outlook.com']

    return '{0}.{1}@{2}'.format(fake.first_name().lower().replace(" ", "."),
                                fake.last_name().lower().replace(" ", "."),
                                random.choice(domains))


def send_mail(mailer, msg_from, msg_to, subject, with_attach):
    msg = email.MIMEMultipart.MIMEMultipart()

    body_text = email.MIMEText.MIMEText("""
    This is a plain text message.  But the HTML message is way better!
    """)

    body_html = email.MIMEText.MIMEText("""
    This is a <b>HTML</b> <i>format</i> email - With inline attachments!
    <br>
    <img src="cid:1234">
    <br>
    """, 'html')

    img_inline = email.MIMEBase.MIMEBase('image', 'png')
    img_inline.set_payload(open('sample-attachment.png').read())
    img_inline.add_header('Content-ID', '<{}>'.format('1234'))
    img_inline.add_header('Content-Disposition', 'inline', filename='sample-attachment.png')
    encoders.encode_base64(img_inline)

    if with_attach:
        img_attach = email.MIMEBase.MIMEBase('image', 'png')
        img_attach.set_payload(open('../images/holdmail-header.png').read())
        img_attach.add_header('Content-Disposition', 'attachment', filename='holdmail-header-attach.png')
        encoders.encode_base64(img_attach)
        msg.attach(img_attach)

    msg.attach(body_text)
    msg.attach(body_html)
    msg.attach(img_inline)

    msg.add_header('From', msg_from)
    msg.add_header('To', msg_to)
    msg.add_header('Subject', subject)

    print("Sent mail {0} -> {1}: {2}".format(msg_from, msg_to, subject))
    mailer.sendmail(msg_from, [msg_to], msg.as_string())


def gen_subject():
    subjects = ["This is my sandbox, I'm not allowed to go in the deep end. ",
                "I'm bembarassed for you.",
                "It tastes like... burning",
                "Go out on a Tuesday? Who am I, Charlie Sheen?",
                "Ow, my eye! I'm not supposed to get pudding in it.",
                "Bake him away, toys.",
                "Oh boy, dinnertime. The perfect break between work and drunk!",
                "Inflammable means flammable? What a country.",
                "My eyes! The goggles do nothing!",
                "Priceless like a mother's love, or the good kind of priceless?",
                "Shoplifting is a victimless crime. Like punching someone in the dark",
                "Yes, but I'd trade it all for a little more",
                "Pfff, English....I'm never going to England",
                "Trying is the first step towards failure.",
                "Facts are meaningless; you can use facts to prove anything that's remotely true!",
                "Here's to alcohol: the source of, and answer to, all of life's problems.",
                "What's the point of having children if you can't buy their love?",
                "I'm gonna party like its on sale for $19.99!",
                "I thought I had an appetite for destruction, but all I wanted was a club sandwich.",
                "Dear Mr. President, there are too many states nowadays. Please eliminate three. I am not a crackpot.",
                "Me fail English?  That's unpossible!",
                "Marge, you can't keep blaming yourself; just blame yourself once, then move on.",
                "Kids, you tried your best and you failed miserably. The lesson is, never try.",
                "If The Flintstones has taught us anything, it's that pelicans can be used to mix cement.",
                "Just because I don't care doesn't mean I don't understand.",
                "Weaseling out of things is important to learn; it's what separates us from the animals... except the weasel.",
                "Stupidity got us into this mess, and stupidity will get us out.",
                "I want to be alone with my thought.",
                "Fame was like a drug, but what was even more like a drug were the drugs.",
                "If God didn't want us to eat animals, then why'd he make them so tasty?",
                "All these guys with six pack abs, and I'm the only one with a keg.",
                "Even communism works... in theory.",
                "Good things don't end in 'eum,' they end in 'mania' or 'teria'.",
                "If he's so smart, how come he's dead?",
                "Getting out of jury duty is easy. The trick is to say you're prejudiced against all races.",
                "I'm normally not a praying man, but if you're up there, please save me Superman.",
                "How is education going to make me smarter?"]

    return random.choice(subjects)


if __name__ == "__main__":
    main()
