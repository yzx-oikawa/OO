#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

int num_of_poly;

struct term
{
    int co;
    int exp;
    struct term *next;
};

struct term polys[21];

void init()
{
    int i;
    for(i = 0;i<21;i++)
    {
        polys[i].exp = -1;
        polys[i].next = NULL;
    }
}
void buildpoly(int c,int e,int index)
{
    struct term *p = &polys[index];

    if(p->exp==-1)
    {
        p->co = c;
        p->exp = e;
        p->next = NULL;
    }
    else
    {
        while(p->next!=NULL)
            p = p->next;
        p->next = (struct term*)malloc(sizeof(struct term));
        p->next->co = c;
        p->next->exp = e;
        p->next->next = NULL;

    }
}

void input()
{
    int length;
    char buffer[10000];

    struct term *p;

    int co = 0, exp = 0;
    int i=0;

    int sign = 1;
    int sign_of_poly = 1;
    num_of_poly = 0;

    fgets(buffer,1000,stdin);
    buffer[strlen(buffer)-1] = '\0';
    length  = strlen(buffer);
   // printf("%d:%s\n",length,buffer);


    if(buffer[0]=='+')
    {
        sign_of_poly = 1;
        i+=3;
    }
    else if(buffer[0]=='-')
    {
        sign_of_poly = 2;
        i+=3;
    }
    else
    {
        i=2;
    }
    while(i<length)
    {
        if(buffer[i]=='+')
        {
             sign = 1;
             i++;
        }
        else if(buffer[i]=='-')
        {
            sign = 2;
            i++;
        }
        //-{(+1,+2),(3,4),(5,6)}+{(1,1)}-{(2,2)}
        while(isdigit(buffer[i]))
        {
            co*=10;
            co+=buffer[i]-'0';
            i++;
        }
        if(sign==2)
            co = -co;
        sign = 1;
        i+=1;

        if(buffer[i]=='+')
        {
             sign = 1;
             i++;
        }
        else if(buffer[i]=='-')
        {
            sign = 2;
            i++;
        }

        while(isdigit(buffer[i]))
        {
            exp*=10;
            exp+=buffer[i]-'0';
            i++;
        }
        if(sign==2)
            exp = -exp;
        sign = 1;
        i+=1;

    //    printf("%dx^%d ",co*((sign_of_poly==1)?1:-1),exp);
        buildpoly(co*((sign_of_poly==1)?1:-1),exp,num_of_poly);
        if(buffer[i]=='}')
        {
            i++;
            if(buffer[i]=='+')
            {
                sign_of_poly = 1;
                i+=3;
            }
            else if(buffer[i]=='-')
            {
                sign_of_poly = 2;
                i+=3;
            }
            else
            {  i+3;
            }

          //  i+=4;//读取下一个多项式
            num_of_poly++;

        //    putchar('\n');
        }
        else
        {
            i+=2;
        }
        co = 0;
        exp = 0;
    }


}

void add(int n,int m)
{
    int i,j;
    struct term *a = &polys[n];
    struct term *b = &polys[m];
    int flag = 0;
    while(b!=NULL)
    {
        while(a!=NULL)
        {
            if(a->exp==b->exp)
            {
                a->co +=b->co;
                flag = 1;
                break;
            }
            a = a->next;
        }
        if(flag==0)
        {
            buildpoly(b->co,b->exp,n);
        }
        b = b->next;
        a = &polys[n];
    }
}

void output(int i)
{
    struct term *p;
    struct term *q;
    int cotemp,exptemp;
    p = &polys[i];
    q = p->next;
    while(p->next!=NULL)
    {
        q = p->next;
        while(q!=NULL)
        {
            if(p->exp>q->exp)
            {
                exptemp = p->exp;
                p->exp = q->exp;
                q->exp = exptemp;

                cotemp = p->co;
                p->co = q->co;
                q->co = cotemp;

            }
            q = q->next;
        }
        p = p->next;
    }


    p = &polys[i];
    while(p!=NULL)
    {
        printf("%dx^%d ",p->co,p->exp);
        p = p->next;
    }
    putchar('\n');
    p = &polys[i];
    putchar('{');
    while(p!=NULL)
    {
        if(p->co!=0)
        {
            printf("(%d,%d)",p->co,p->exp);
        }

        if(p->next==NULL)
        {
            putchar('}');
            break;
        }
        else
        {
            if(p->co!=0)
                putchar(',');
            p = p->next;

        }
    }

}

int main()
{
    struct term *p;
    int i;
    init();
    input();
    for(i = 1;i<num_of_poly;i++)
    {
        add(0,i);
    }
    output(0);


    return 0;
}
