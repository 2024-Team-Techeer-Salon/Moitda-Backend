FROM nginx

EXPOSE 80

#RUN rm /etc/nginx/nginx.deploy.conf

COPY nginx.conf /etc/nginx/nginx.conf

CMD ["nginx", "-g", "daemon off;"]